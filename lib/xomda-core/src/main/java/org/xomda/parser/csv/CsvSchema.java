package org.xomda.parser.csv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.xomda.parser.ParseContext;
import org.xomda.parser.csv.type.ValueParser;
import org.xomda.shared.logging.LogService;

/**
 * The CSV Schema is parsed out of the first lines of a CSV file (followed by a
 * empty CSV line). It's an {@link Iterable} of {@link CsvSchemaObject CsvSchemaObjects} which
 * are then used later on to figure out how to parse CSV lines into {@link CsvObject CsvObjects}.
 */
public class CsvSchema implements Iterable<CsvSchemaObject> {

	private static final Logger logger = LogService.getLogger(CsvSchema.class);

	private final List<CsvSchemaObject> objects = new ArrayList<>();

	@Override
	public Iterator<CsvSchemaObject> iterator() {
		return objects.iterator();
	}

	public Stream<CsvSchemaObject> stream() {
		return objects.stream();
	}

	public void addModel(final CsvSchemaObject schemaObject) {
		objects.add(schemaObject);
	}

	public CsvObject readObject(final CSVRecord record, final ParseContext context) {
		final String name = record.get(0);
		final CsvSchemaObject csvSchemaObject = stream()
				.filter((final CsvSchemaObject obj) -> name.equals(obj.getName()))
				.findFirst().orElse(null);

		if (null == csvSchemaObject) {
			return null;
		}

		final CsvObject obj = new CsvObject(csvSchemaObject.getObjectClass());

		IntStream.range(0, csvSchemaObject.getAttributes().size()).forEach(i -> {
			final CsvSchemaObjectAttribute attr = csvSchemaObject.getAttributes().get(i);
			final int csvIndex = attr.getIndex();
			final String value = record.isSet(csvIndex) ? record.get(csvIndex) : "";
			final ValueParser valueParser = attr.getSetter();
			final Runnable setVal = valueParser instanceof ValueParser.Optional
					? () -> ((Optional<?>) valueParser.apply(value.isEmpty() ? null : value))
					.ifPresent(v -> obj.setValue(attr.getName(), v))
					: () -> obj.setValue(attr.getName(), valueParser.apply(value.isEmpty() ? null : value));
			if (valueParser instanceof ValueParser.Primitive) {
				setVal.run();
			} else {
				context.runDeferred(setVal);
			}
		});
		return obj;
	}

	public static CsvSchema load(final Iterator<CSVRecord> it, final ParseContext context) {
		logger.trace("Reading schema");

		final CsvSchema schema = new CsvSchema();
		CSVRecord record = null;

		// skip empty lines
		while (it.hasNext() && CsvService.isEmpty(record = it.next())) {
			// noop, skip empty lines
		}

		// if nothing's found, there are no records anymore
		if (null == record) {
			return schema;
		}

		// process the schema
		do {
			final CsvSchemaObject schemaObject = new CsvSchemaObject(record, context);
			logger.debug("Found {} ({})", schemaObject.getName(), schemaObject.getObjectClass());
			schema.addModel(schemaObject);
		} while (it.hasNext() && !CsvService.isEmpty(record = it.next()));

		// return the schema
		return schema;
	}

	public boolean isCompatible(CsvSchema other) {
		return objects.stream().allMatch(csvSchemaObject -> other.objects.stream().anyMatch(
						ocso -> Objects.equals(csvSchemaObject.getObjectClass(), ocso.getObjectClass())
				) || other.objects.stream().noneMatch(
						ocso -> Objects.equals(csvSchemaObject.getName(), ocso.getName())
				)
		);
	}

}
