package org.xomda.core.csv;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CsvObject {

    final Class<?>[] classes;

    private final org.xomda.core.csv.CsvObjectState state = new org.xomda.core.csv.CsvObjectState();

    private Object proxy;

    public CsvObject(final Class<?>[] classes) {
        if (null == classes || classes.length < 1) {
            throw new IllegalArgumentException("Specify at least one class");
        }
        this.classes = classes;
    }

    public CsvObject(final Class<?> clazz) {
        this(new Class<?>[]{clazz});
    }

    public org.xomda.core.csv.CsvObjectState getState() {
        return state;
    }

    public <T> T getValue(final String name) {
        @SuppressWarnings("unchecked")
        final T t = (T) getState().get(name);
        return t;
    }

    public <T> T setValue(final String name, final T value) {
        if (null == value) return null;
        @SuppressWarnings("unchecked")
        final T t = (T) getState().put(name, value);
        return t;
    }

    public <T> T computeIfAbsent(final String name, final Supplier<T> supplier) {
        @SuppressWarnings("unchecked")
        final T result = (T) getState().computeIfAbsent(name, k -> supplier.get());
        return result;
    }

    public Object getProxy() {
        return Optional.ofNullable(proxy).orElseGet(this::createProxy);
    }

    private synchronized Object createProxy() {
        if (null != proxy) return proxy;
        return proxy = org.xomda.core.csv.CsvProxyObject.create(this);
    }

    public Class<?>[] getClasses() {
        return classes;
    }

    public boolean isInstance(final Class<?> clazz) {
        return null != classes && Arrays.stream(classes).anyMatch(Predicate.isEqual(clazz));
    }

    public boolean isEnum() {
        return null != classes && Arrays.stream(classes).anyMatch(c -> c.isAssignableFrom(Enum.class));
    }

}
