package org.xomda.core.module;

import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.xomda.core.csv.CsvObject;
import org.xomda.core.csv.CsvSchema;
import org.xomda.core.csv.CsvSchemaObject;
import org.xomda.core.extension.CsvObjectProcessor;
import org.xomda.core.extension.CsvSchemaProcessor;
import org.xomda.core.util.ParseContext;
import org.xomda.shared.util.ReflectionUtils;
import org.xomda.shared.util.StringUtils;

public class XOmdaReverseEntity implements CsvObjectProcessor, CsvSchemaProcessor {

    private final Map<CsvSchemaObject, List<CsvSchemaObject>> rev = new ConcurrentHashMap<>();
    private final Deque<CsvObject> stack = new ArrayDeque<>();

    private ParseContext context = null;
    private CsvSchema schema = null;

    @Override
    public void init(final ParseContext context) {
        this.context = context;
        rev.clear();
        stack.clear();
    }

    @Override
    public void process(final CsvSchema schema) {
        Objects.requireNonNull(context, "The config should not be null at this point.");
        this.schema = schema;
        schema.stream().forEach((final CsvSchemaObject schemaObject) -> {
            final String name = schemaObject.getName();
            final String listIdentifier = StringUtils.toPascalCase(name + " List");
            schema.stream().forEach((final CsvSchemaObject otherObject) -> {
                String parentIdentifier = StringUtils.toPascalCase(otherObject.getName());
                // getChildList()
                ReflectionUtils.getGetter(otherObject.getObjectClass(), listIdentifier)
                    // getParent()
                    .filter(l -> ReflectionUtils.getGetter(schemaObject.getObjectClass(), parentIdentifier).isPresent())
                    .ifPresent((final Method method) ->
                        rev.computeIfAbsent(schemaObject, o -> new ArrayList<>()).add(otherObject)
                    );

            });
        });
    }

    @Override
    public void process(final CsvObject object) {
        Objects.requireNonNull(context, "The config should not be null at this point.");
        Objects.requireNonNull(schema, "The CSV Schema should not be null at this point.");
        schema.stream()
            .filter(o -> o.isInstance(object))
            .findFirst()
            .ifPresent(schemaObject -> rev
                .get(schemaObject).stream()
                // only add it to one parent
                .findFirst().ifPresent(cso -> {
                    final Class<?> reverseClass = cso.getObjectClass();
                    CsvObject peek;
                    do {
                        peek = stack.poll();
                    } while (null != peek && !peek.isInstance(reverseClass));

                    if (peek != null) {
                        // set parent
                        final CsvObject parentObject = peek;
                        schema.stream().filter(o -> o.isInstance(parentObject)).findFirst().ifPresent(pp -> {
                            final String parentPropName = StringUtils.toCamelCase(pp.getName());
                            object.setValue(parentPropName, parentObject.getProxy());
                        });

                        // reverse list (list on container object) to add the object to
                        final String propName = StringUtils.toCamelCase(schemaObject.getName() + " List");
                        peek.computeIfAbsent(propName, ArrayList::new).add(object.getProxy());

                        // push the peek again
                        stack.push(peek);
                    }
                    // add the new object
                    stack.push(object);
                }));
    }

}
