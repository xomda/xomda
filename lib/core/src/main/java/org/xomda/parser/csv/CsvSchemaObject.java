package org.xomda.parser.csv;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVRecord;
import org.xomda.core.java.JavaUtils;
import org.xomda.core.util.Extensions;
import org.xomda.parser.ParseContext;
import org.xomda.parser.csv.type.TypeFactory;
import org.xomda.parser.csv.type.ValueParser;
import org.xomda.shared.util.ReflectionUtils;

/**
 * Represents a single CSV line in the {@link CsvSchema}. It also knows which
 * java interface it's bound to.
 */
public class CsvSchemaObject {

	private final Class<?> clazz;
	private final String name;

	private final List<CsvSchemaObjectAttribute> attributes = new ArrayList<>();

	CsvSchemaObject(final CSVRecord record, final ParseContext context) {
		if (record.size() < 1) {
			throw new IllegalArgumentException(
					"A schema object should contain at least one field which specifies the name of the object.");
		}

		String recordName = record.get(0);
		clazz = getObjectClass(recordName, context.getConfig().getClasspath())
				.orElseThrow(() -> new NoSuchElementException("Could not find appropriate class for " + recordName));

		// if the name is fully qualified, we should trim off the package name
		if (JavaUtils.hasPackage(recordName)) {
			name = JavaUtils.getClassName(recordName);
		} else {
			name = recordName;
		}

		final TypeFactory typeFactory = new TypeFactory().register(Extensions.getValueParserProviders(context.getConfig()));

		IntStream.range(1, record.size()).filter(i -> !record.get(i).isBlank()).forEach(i -> {
			final String propName = record.get(i);
			final Optional<Method> getter = ReflectionUtils.getGetter(clazz, propName);
			final Optional<Class<?>> type = getter.map(Method::getReturnType);
			final ValueParser valueParser = typeFactory.getSetter(type.isPresent() ? type.get() : String.class);
			final CsvSchemaObjectAttribute attr = new CsvSchemaObjectAttribute(propName, i, valueParser);
			attributes.add(attr);
		});
	}

	public String getName() {
		return name;
	}

	public Class<?> getObjectClass() {
		return clazz;
	}

	/**
	 * Tries to find the matching class for the given name (and classpaths)
	 */
	private static Optional<Class<?>> getObjectClass(final String name, final String[] classpath) {
		return getClassNameCandidates(name, classpath)
				.map(ReflectionUtils::findClass)
				.filter(Optional::isPresent)
				.findFirst()
				.orElseGet(Optional::empty)
				.map(Function.identity());
	}

	/**
	 * Returns a stream of fully qualified class names,
	 * which then can be looked for using the by the classloader.
	 * <p>
	 * If the name happens to be fully qualified already,
	 * then a <code>Stream.of(name)</code> will be returned.
	 */
	private static Stream<String> getClassNameCandidates(final String name, final String[] classpath) {
		return JavaUtils.hasPackage(name)
				// if the name if fully qualified, just take that one
				? Stream.of(name)
				// otherwise try to scan the defined locations
				: Stream.of(classpath).map(cp -> cp + "." + name);
	}

	public List<CsvSchemaObjectAttribute> getAttributes() {
		return attributes;
	}

	public boolean isInstance(final CsvObject obj) {
		return Stream.of(obj.getClasses()).anyMatch(Predicate.isEqual(getObjectClass()));
	}

}
