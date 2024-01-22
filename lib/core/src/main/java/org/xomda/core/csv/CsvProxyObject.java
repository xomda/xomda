package org.xomda.core.csv;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.xomda.shared.util.StringUtils;

class CsvProxyObject implements InvocationHandler {

    private final org.xomda.core.csv.CsvObjectState state;

    CsvProxyObject(org.xomda.core.csv.CsvObjectState state) {
        this.state = state;
    }

    static Object create(org.xomda.core.csv.CsvObject parent) {
        CsvProxyObject p = new CsvProxyObject(parent.getState());
        return Proxy.newProxyInstance(parent.getClass().getClassLoader(), parent.classes, p);
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final String name = method.getName();
        if ("equals".equals(name)) {
            return Stream.of(args).allMatch(a -> a == proxy);
        }
        if ("toString".equals(name)) {
            String className = Arrays
                .stream(proxy.getClass().getInterfaces())
                .map(Class::getSimpleName)
                .collect(Collectors.joining("|"));
            return className + "{" + state + "}";
        }
        if (name.startsWith("get") && name.length() > 3) {
            final String propName = StringUtils.toLower1(name.substring(3));
            final Object result = state.get(propName);
            return result instanceof org.xomda.core.csv.CsvObject csvObject
                ? csvObject.getProxy()
                : result;
        }
        return Void.TYPE;
    }

}