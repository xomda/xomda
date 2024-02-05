package org.xomda.core.java;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.xomda.shared.util.ReflectionUtils;

public class JavaImportService {

	private final Map<String, String> imports = new ConcurrentHashMap<>();
	private final String localClass;

	private final List<String> sortOrder = Arrays.asList("static java.", "static", "java.");

	public JavaImportService(final String localClass) {
		if (!JavaUtils.isValidClassName(localClass)) {
			throw new IllegalArgumentException("Invalid class name:" + localClass);
		}
		this.localClass = localClass;
	}

	public String getLocalClassName() {
		return localClass;
	}

	public String addImport(final Class<?> clazz) {
		Objects.requireNonNull(clazz);
		return addImport(ReflectionUtils.getBareType(clazz).getName());
	}

	public String addImport(final String fullyQualifiedClassName) {
		return addImport(fullyQualifiedClassName, false);
	}

	public String addStaticImport(final String methodName) {
		return addImport(methodName, true);
	}

	public String addStaticImport(final Class<?> clazz, final String methodName) {
		Objects.requireNonNull(clazz);
		Objects.requireNonNull(methodName);
		return addStaticImport(ReflectionUtils.getBareType(clazz).getName() + "." + methodName);
	}

	public String addImport(final String fullyQualifiedClassName, final boolean isStatic) {
		Objects.requireNonNull(fullyQualifiedClassName);

		if (!JavaUtils.hasPackage(fullyQualifiedClassName)) {
			return fullyQualifiedClassName;
		}

		final String className = JavaUtils.getClassName(fullyQualifiedClassName);
		final String registeredClassName = (isStatic ? "static " : "") + fullyQualifiedClassName;

		if (imports.containsKey(className)) {
			return imports.get(className).equals(fullyQualifiedClassName) ? className : fullyQualifiedClassName;
		}

		final boolean isJavaLang = JavaUtils.isGlobal(fullyQualifiedClassName);
		final boolean existsInJavaLang = existsInJavaLang(fullyQualifiedClassName);

		// Return full Class names which also exist in java.lang
		if (!isJavaLang && existsInJavaLang) {
			return fullyQualifiedClassName;
		}

		// don't import java.lang classes
		if (isJavaLang) {
			return className;
		}

		// Same package? Doesn't need to be imported
		if (isSamePackage(fullyQualifiedClassName)) {
			// still remember that we are using a class from the same package
			imports.put(className, registeredClassName);
			return className;
		}

		imports.put(className, registeredClassName);
		return className;
	}

	public List<String> getSortOrder() {
		return sortOrder;
	}

	boolean existsInJavaLang(final String className) {
		return ReflectionUtils.findClass("java.lang." + JavaUtils.getClassName(className)).isPresent();
	}

	boolean isSamePackage(final String className) {
		return JavaUtils.isSamePackage(getLocalClassName(), className);
	}

	private Comparator<String> getComparator() {
		return sortOrder.stream().map(JavaImportService::compareBeginsWith)
				.reduce((a, b) -> 0, Comparator::thenComparing).thenComparing(String::compareTo);
	}

	public Stream<String> stream() {
		return imports.values().stream().filter(c -> !JavaUtils.isSamePackage(c, getLocalClassName()))
				.sorted(getComparator());
	}

	public void forEach(final Consumer<String> consumer) {
		forEach(consumer, () -> {
			/* noop */
		});
	}

	public void forEach(final Consumer<String> consumer, final Runnable newGroupHandler) {
		final AtomicReference<String> lastRef = new AtomicReference<>();
		stream().forEach((final String imp) -> {
			final String last = lastRef.getAndUpdate(s -> imp);
			if (last != null && getSortOrder().stream().anyMatch(s -> last.startsWith(s) && !imp.startsWith(s))) {
				newGroupHandler.run();
			}
			consumer.accept(imp);
		});
	}

	public boolean isEmpty() {
		return imports.isEmpty();
	}

	private static Comparator<String> compareBeginsWith(final String startsWith) {
		return (a, b) -> {
			final boolean isA = a.startsWith(startsWith);
			final boolean isB = b.startsWith(startsWith);
			return isA ? isB ? a.compareTo(b) : -1 : isB ? 1 : 0;
		};
	}

}
