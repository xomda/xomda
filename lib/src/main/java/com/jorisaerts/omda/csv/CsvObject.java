package com.jorisaerts.omda.csv;

import com.jorisaerts.omda.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class CsvObject {

    final Class<?>[] classes;

    private final Map<String, Object> state = new ConcurrentHashMap<>();
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

    public Map<String, Object> getState() {
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

    public Object getProxy() {
        return Optional.ofNullable(proxy).orElseGet(this::createProxy);
    }

    private synchronized Object createProxy() {
        return null != proxy ? proxy : Proxy.newProxyInstance(getClass().getClassLoader(), classes, (final Object proxy, final Method method, final Object[] args) -> {
            final String name = method.getName();

            if ("toString".equals(method.getName())) {
                return "Proxy[" + state + "]";
            }

            if (name.startsWith("get")) {
                final String propName = StringUtils.toLower1(name.substring(3));
                final Object result = state.get(propName);
                return result;
            }

            return Void.TYPE;
        });
    }

    public Class<?>[] getClasses() {
        return classes;
    }

    public boolean isInstance(final Class<?> clazz) {
        return null != classes && Arrays.stream(classes).anyMatch(Predicate.isEqual(clazz));
    }

}
