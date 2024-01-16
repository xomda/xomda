package com.jorisaerts.omda.csv;

import com.jorisaerts.omda.util.ModelContext;
import com.jorisaerts.omda.util.ReflectionUtils;
import com.jorisaerts.omda.util.StringUtils;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class CsvSchema {

    private static final Logger logger = LogManager.getLogger(CsvSchema.class);

    private final List<CsvSchemaObject> objects = new ArrayList<>();

    private final Map<CsvSchemaObject, List<CsvSchemaObject>> rev = new ConcurrentHashMap<>();

    public boolean addModel(final CsvSchemaObject schemaObject) {
        return objects.add(schemaObject);
    }

    public CsvObject readObject(final CSVRecord record) {
        final String name = record.get(0);
        final CsvSchemaObject csvSchemaObject = objects.stream().filter((CsvSchemaObject obj) -> name.equals(obj.getName()))
                .findFirst()
                .orElse(null);

        if (null == csvSchemaObject) return null;

        final CsvObject obj = new CsvObject(csvSchemaObject.getObjectClass());

        IntStream
                .range(0, csvSchemaObject.getAttributes().size())
                .forEach(i -> {
                    final CsvSchemaObjectAttribute attr = csvSchemaObject.getAttributes().get(i);
                    final int csvIndex = attr.getIndex();
                    final String value = record.isSet(csvIndex) ? record.get(csvIndex) : "";
                    obj.setValue(attr.getName(), attr.getSetter().apply((value.isEmpty() ? null : value)));
                });

        return obj;
    }


    public CsvObject addObject(final CsvObject obj, final Deque<CsvObject> stack) {
        objects
                .stream()
                .filter(o -> o.isParent(obj))
                .findFirst()
                .ifPresent((schemaObject -> {
                    rev.get(schemaObject).stream().findFirst().ifPresent((cso) -> {
                        final Class<?> reverseClass = cso.getObjectClass();
                        CsvObject peek;
                        do {
                            peek = stack.poll();
                        } while (null != peek && !peek.isInstance(reverseClass));

                        if (peek != null) {
                            final String propName = StringUtils.toCamelCase(schemaObject.getName() + " List");
                            if (null == peek.getValue(propName)) {
                                peek.setValue(propName, new ArrayList<>());
                            }
                            ((ArrayList<Object>) peek.getValue(propName)).add(obj);
                            // push the peek again
                            stack.push(peek);
                        }

                        // add the new object
                        stack.push(obj);
                    });
                }));

        final CsvObject peek = stack.peek();
        if (null == peek) return null;

        return null;
    }


    public static CsvSchema load(final Iterator<CSVRecord> it, final ModelContext context) {
        logger.info("Reading schema");

        final CsvSchema schema = new CsvSchema();
        CSVRecord record = null;

        // skip empty lines
        while (it.hasNext() && CsvService.isEmpty(record = it.next())) {
            // noop, skip empty lines
        }

        // if nothing's found, there are no records anymore
        if (null == record) return schema;

        // process the schema
        do {
            final CsvSchemaObject schemaObject = new CsvSchemaObject(record, context);
            logger.info("Found {} ({})", schemaObject.getName(), schemaObject.getObjectClass());
            schema.addModel(schemaObject);
        } while (it.hasNext() && !CsvService.isEmpty(record = it.next()));

        // reverse master
        schema.objects.forEach((final CsvSchemaObject schemaObject) -> {
            final String name = schemaObject.getName();
            final String identifier = StringUtils.toPascalCase(name + " List");
            schema.objects.forEach((final CsvSchemaObject otherObject) -> ReflectionUtils
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
