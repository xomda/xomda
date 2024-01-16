package com.jorisaerts.omda.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class ReflectionUtils {


    public static Function<Object, Object> getGetterConsumer(final Class<?> clazz, final String name) {
        return getGetter(clazz, name).<Function<Object, Object>>map(method -> (final Object obj) -> {
            try {
                return method.invoke(obj);
            } catch (final IllegalAccessException | InvocationTargetException e) {
                // throw new RuntimeException(e);
                return null;
            }
        }).orElseGet(() -> (final Object obj) -> {
            return null;
        });

    }

    public static Optional<Method> getGetter(final Class<?> clazz, final String name) {
        if (null == name || name.isBlank()) {
            throw new IllegalArgumentException("The name should not be blank or null");
        }
        final String trimmed = name.trim();
        final String getterName = "get" + (Character.toUpperCase(trimmed.charAt(0))) + trimmed.substring(1);
        return Arrays
                .stream(clazz.getDeclaredMethods())
                .filter(isGetter(getterName))
                .findFirst();
    }

    private static Predicate<Method> isGetter(final String methodName) {
        return (Method method) -> method.getParameterCount() == 0 && (method.getName().equals(methodName));
    }


}
