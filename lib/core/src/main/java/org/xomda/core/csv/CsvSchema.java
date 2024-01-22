package org.xomda.core.csv;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.xomda.shared.logging.LogService;
import org.xomda.shared.util.ReflectionUtils;
import org.xomda.shared.util.StringUtils;
import org.xomda.core.csv.type.ValueParser;
import org.xomda.core.util.ParseContext;

public class CsvSchema implements Iterable<org.xomda.core.csv.CsvSchemaObject> {

    private static final Logger logger = LogService.getLogger(CsvSchema.class);

    private final List<org.xomda.core.csv.CsvSchemaObject> objects = new ArrayList<>();

    private final Map<org.xomda.core.csv.CsvSchemaObject, List<org.xomda.core.csv.CsvSchemaObject>> rev = new ConcurrentHashMap<>();

    @Override
    public Iterator<org.xomda.core.csv.CsvSchemaObject> iterator() {
        return objects.iterator();
    }

    public Stream<org.xomda.core.csv.CsvSchemaObject> stream() {
        return objects.stream();
    }

    public void addModel(final org.xomda.core.csv.CsvSchemaObject schemaObject) {
        objects.add(schemaObject);
    }

    public org.xomda.core.csv.CsvObject readObject(final CSVRecord record, final ParseContext context) {
        final String name = record.get(0);
        final org.xomda.core.csv.CsvSchemaObject csvSchemaObject = stream()
            .filter((org.xomda.core.csv.CsvSchemaObject obj) -> name.equals(obj.getName()))
            .findFirst()
            .orElse(null);

        if (null == csvSchemaObject) return null;

        final org.xomda.core.csv.CsvObject obj = new org.xomda.core.csv.CsvObject(csvSchemaObject.getObjectClass());

        IntStream
            .range(0, csvSchemaObject.getAttributes().size())
            .forEach(i -> {
                final org.xomda.core.csv.CsvSchemaObjectAttribute attr = csvSchemaObject.getAttributes().get(i);
                final int csvIndex = attr.getIndex();
                final String value = record.isSet(csvIndex) ? record.get(csvIndex) : "";
                ValueParser valueParser = attr.getSetter();
                Runnable setVal = valueParser instanceof ValueParser.Optional
                    ? () -> ((Optional<?>) valueParser.apply(value.isEmpty() ? null : value)).ifPresent(v -> obj.setValue(attr.getName(), v))
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
        while (it.hasNext() && org.xomda.core.csv.CsvService.isEmpty(record = it.next())) {
            // noop, skip empty lines
        }

        // if nothing's found, there are no records anymore
        if (null == record) return schema;

        // process the schema
        do {
            final org.xomda.core.csv.CsvSchemaObject schemaObject = new org.xomda.core.csv.CsvSchemaObject(record, context);
            logger.debug("Found {} ({})", schemaObject.getName(), schemaObject.getObjectClass());
            schema.addModel(schemaObject);
        } while (it.hasNext() && !org.xomda.core.csv.CsvService.isEmpty(record = it.next()));

        // reverse master
        schema.objects.forEach((final org.xomda.core.csv.CsvSchemaObject schemaObject) -> {
            final String name = schemaObject.getName();
            final String identifier = StringUtils.toPascalCase(name + " List");
            schema.objects.forEach((final org.xomda.core.csv.CsvSchemaObject otherObject) -> ReflectionUtils
                .getGetter(otherObject.getObjectClass(), identifier)
                .ifPresent((final Method method) ->
                    schema.rev.computeIfAbsent(schemaObject, o -> new ArrayList<>())
                        .add(otherObject)
                )
            );
        });

        // return the schema
        return schema;
    }

}
