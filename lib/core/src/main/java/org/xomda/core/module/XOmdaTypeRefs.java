package org.xomda.core.module;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.xomda.core.csv.CsvObject;
import org.xomda.core.csv.CsvSchema;
import org.xomda.core.csv.type.ValueParser;
import org.xomda.core.extension.CsvSchemaProcessor;
import org.xomda.core.extension.ValueParserProvider;
import org.xomda.core.util.InternalParseContext;
import org.xomda.core.util.ParseContext;
import org.xomda.shared.util.ReflectionUtils;

public class XOmdaTypeRefs implements ValueParserProvider, CsvSchemaProcessor {

    private ParseContext context = null;
    private CsvSchema schema = null;

    @Override
    public void init(final ParseContext context) {
        this.context = context;
    }

    @Override
    public void process(final CsvSchema schema) {
        Objects.requireNonNull(context, "The config should not be null at this point.");
        this.schema = schema;
    }

    @Override
    public boolean test(final Class<?> type) {
        // we don't have access to the schema yet
        // that is not okay, we need to know the objects here
        // enums will be parsed elsewhere
        return !type.isEnum() && Stream
            .of(context.getConfig().getClasspath())
            .anyMatch(type.getPackageName()::startsWith);
    }

    @Override
    public ValueParser apply(final Class<?> type) {
        return (String stringValue) -> {
            if (null == stringValue || stringValue.isEmpty()) return null;
            return resolve(type, stringValue);
        };
    }

    private <T> T resolve(final Class<?> type, String ref) {
        Function<Object, String> fn = ((InternalParseContext) context).getCache().stream()
            .map(o -> {
                Class<?> oClz = o.getClasses() == null || o.getClasses().length < 1
                    ? null
                    : o.getClasses()[0];
                Function<?, ?> nameGetter = ReflectionUtils.getGetterFunction(oClz, "Name");
                @SuppressWarnings("unchecked")
                Function<Object, String> result = (Function<Object, String>) nameGetter;
                return result;
            })
            .filter(Objects::nonNull)
            .findFirst()
            .orElse((Object o) -> "");

        String[] parts = ref.split("\\/");
        List<CsvObject> objects = context.getCache();

        @SuppressWarnings("unchecked")
        T result = (T) findByKey(objects.stream(), parts)
            .findFirst()
            .map(CsvObject::getProxy)
            .orElse(null);

        return result;
    }

    private boolean isOmdaObject(Object o) {
        Class<?> oClass = o.getClass();
        return schema.stream().anyMatch(f ->
            Stream.concat(
                Stream.of(oClass),
                Stream.of(oClass.getInterfaces())
            ).anyMatch(
                i -> i.isAssignableFrom(f.getObjectClass())
            )
        );
    }

    private Stream<CsvObject> findByKey(final Stream<CsvObject> rootObjects, final String[] parts) {
        if (null == parts || parts.length == 0) return Stream.empty();
        final String key = parts[0];
        return rootObjects
            .filter(csvObject -> key.equals(csvObject.getValue("name")))
            .flatMap(csvObject -> {
                String[] subKeys = Arrays.copyOfRange(parts, 1, parts.length);
                return subKeys.length > 0
                    ? findByKey(getChildren(csvObject), subKeys)
                    : Stream.of(csvObject);
            });
    }

    private Stream<CsvObject> getChildren(CsvObject csvObject) {
        return csvObject.getState().values()
            .stream()
            .filter(Iterable.class::isInstance)
            .map(Iterable.class::cast)
            .filter(it -> it.iterator().hasNext())
            .flatMap(it -> {
                Object next = it.iterator().next();
                if (!isOmdaObject(next)) return Stream.empty();
                @SuppressWarnings("unchecked")
                Stream<CsvObject> children = StreamSupport
                    .stream(it.spliterator(), true)
                    .map(this::getCsvObject)
                    .filter(o -> ((Optional<?>) o).isPresent())
                    .map(o -> ((Optional<?>) o).get());
                return children;
            });
    }

    private Optional<CsvObject> getCsvObject(Object proxy) {
        return context.getCache()
            .stream()
            .filter(o -> o.getProxy() == proxy)
            .findFirst();
    }

}
