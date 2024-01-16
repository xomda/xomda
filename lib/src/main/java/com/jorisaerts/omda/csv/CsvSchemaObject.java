package com.jorisaerts.omda.csv;

import com.jorisaerts.omda.util.ModelContext;
import com.jorisaerts.omda.util.ReflectionUtils;
import com.jorisaerts.omda.util.StringUtils;
import org.apache.commons.csv.CSVRecord;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CsvSchemaObject {

    private final Class<?> clazz;
    private final String name;

    private final List<CsvSchemaObjectAttribute> attributes = new ArrayList<>();

    CsvSchemaObject(final CSVRecord record, final ModelContext context) {
        if (record.size() < 1)
            throw new IllegalArgumentException("A schema object should contain at least one field which specifies the name of the object.");

        this.name = record.get(0);
        this.clazz = getObjectClass(name, context.getClasspath())
                .orElseThrow(() -> new NoSuchElementException("Could not find appropriate class for " + name));

        IntStream.range(1, record.size())
                .filter(i -> !record.get(i).isEmpty())
                .forEach(i -> {
                    final String propName = record.get(i);
                    final Optional<Method> getter = ReflectionUtils.getGetter(clazz, propName);
                    final Optional<Class<?>> type = getter.map(Method::getReturnType);
                    final CsvTypeFactory.Setter setter = CsvTypeFactory.getSetter(type.isPresent() ? type.get() : String.class);
                    final CsvSchemaObjectAttribute attr = new CsvSchemaObjectAttribute(propName, i, setter);
                    attributes.add(attr);
                });
    }

    public String getName() {
        return name;
    }

    public Class<?> getObjectClass() {
        return clazz;
    }

    public String getIdentifier() {
        return StringUtils.toCamelCase(getName());
    }

    private static Optional<Class<?>> getObjectClass(final String name, final String[] classpath) {
        Class<?> found = null;
        for (final String cp : classpath) {
            try {
                found = Class.forName(cp + "." + name);
            } catch (final ClassNotFoundException e) {

            }
        }
        return Optional.ofNullable(found);
    }

    public List<CsvSchemaObjectAttribute> getAttributes() {
        return attributes;
    }

    public boolean isParent(final CsvObject obj) {
        return Stream.of(obj.getClasses()).anyMatch(Predicate.isEqual(getObjectClass()));
    }

}
