package com.jorisaerts.omda.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProxyObject {

    private interface GetSet {
        String getName();

        Object get(Object subject) throws ReflectiveOperationException;

        void set(Object subject, Object value) throws ReflectiveOperationException;
    }

    private static boolean isGetter(final Method method) {
        final String name = method.getName();
        final boolean isPublic = ((method.getModifiers() & (Modifier.PUBLIC)) == Modifier.PUBLIC);

        if (!isPublic || method.getParameterCount() > 0) return false;
        return name.startsWith("get")
                && name.length() > 3
                && !Void.TYPE.equals(method.getReturnType())
                && Character.isUpperCase(name.substring(3).charAt(0))
                ;
    }

    private static Optional<Method> getSetter(final Method method) {
        final String baseName = method.getName().substring(3);
        final Class<?> methodType = method.getReturnType();
        try {
            final Method setter = (method.getDeclaringClass().getDeclaredMethod("set" + baseName, methodType));
            if (Void.TYPE.equals(setter.getReturnType())) {
                return Optional.of(setter);
            }
        } catch (final NoSuchMethodException e) {
            // no setter found
        }
        return Optional.empty();
    }


    private static Map<String, GetSet> toMap(final Class<?> someInterface) throws IllegalArgumentException, SecurityException {
        final Method[] methods = someInterface.getDeclaredMethods();
        final Map<String, Object> props = new ConcurrentHashMap<>();
        @SuppressWarnings("unchecked")
        final Stream<GetSet> gettersAndSetters = (Stream<GetSet>) Arrays.stream(methods)
                .map((final Method method) -> {
                    final String name = method.getName();
                    final boolean isPublic = ((method.getModifiers() & (Modifier.PUBLIC)) == Modifier.PUBLIC);

                    // only public methods
                    if (!isPublic || !isGetter(method)) return Optional.empty();

                    // find setter
                    final Optional<Method> setterCandidate = getSetter(method);
                    if (setterCandidate.isEmpty()) return Optional.empty();

                    final Method setter = setterCandidate.get();

                    return Optional.of(createGetterSetter(method, setter));
                })
                .filter(Optional::isPresent)
                .map(Optional::get);
        return gettersAndSetters.collect(Collectors.toMap(
                GetSet::getName, Function.identity()
        ));
    }

    private static GetSet createGetterSetter(final Method getter, final Method setter) {

        final AtomicReference<Object> value = new AtomicReference<>();

        return new GetSet() {

            @Override
            public String getName() {
                return getter.getName().substring(3);
            }

            @Override
            public Object get(final Object subject) throws InvocationTargetException, IllegalAccessException {
                return value.get();
            }

            @Override
            public void set(final Object subject, final Object newValue) throws InvocationTargetException, IllegalAccessException {
                value.set(newValue);
            }

        };
    }

    public static <I> I create(final Class<I> iface) {
        final Map<String, GetSet> map = toMap(iface);

        @SuppressWarnings("unchecked")
        final I instance = (I) Proxy.newProxyInstance(iface.getClassLoader(), new Class<?>[]{iface}, (final Object proxy, final Method method, final Object[] args) -> {
            final String name = method.getName();

            if ("toString".equals(name)) {
                return iface.getName();
            }

            if (name.length() <= 3) {
                return Void.TYPE;
            }
            final String baseName = name.substring(3);
            final GetSet getSet = map.get(baseName);

            if (null == getSet) {
                return Void.TYPE;
            }

            if ("get".equals(name.substring(0, 3))) {
                return getSet.get(proxy);
            } else {
                getSet.set(proxy, args[0]);
                return Void.TYPE;
            }

        });

        return instance;
    }

}
