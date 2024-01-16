package com.jorisaerts.omda.csv;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvTypeFactory {

    public interface Setter extends Function<String, Object> {
    }

    public static Setter getSetter(final Class<?> type) {
        if (null == type) return s -> s;
        if (type.equals(int.class) || type.equals(Integer.class)) {
            return (Integer::parseInt);
        } else if (type.equals(double.class) || type.equals(Double.class)) {
            return (Double::parseDouble);
        } else if (type.equals(float.class) || type.equals(Float.class)) {
            return (Float::parseFloat);
        } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            return (Boolean::parseBoolean);
        } else if (type.isEnum()) {
            return getEnumMap(type)::get;
        }
        return s -> s;
    }

    private static Map<String, Object> getEnumMap(final Class<?> enumClazz) {
        if (!enumClazz.isEnum()) return Collections.emptyMap();
        final Object[] enumValues = enumClazz.getEnumConstants();
        try {
            final Method m = enumClazz.getMethod("name");
            return Stream.of(enumValues).collect(Collectors.toMap(v -> {
                try {
                    return (String) m.invoke(v);
                } catch (final IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }, Function.identity()));
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> Function<String, T> rawCast(final Function<String, Object> f) {
        @SuppressWarnings("unchecked")
        final Function<String, T> r = (Function<String, T>) f;
        return r;
    }

}
