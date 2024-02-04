package org.xomda.core.csv;

import java.lang.reflect.Proxy;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This object is a {@link java.util.Map Map&lt;String, Object&gt;} which holds all properties of a .
 */
public class CsvObjectState extends ConcurrentHashMap<String, Object> {

    @Override
    public String toString() {
        final StringJoiner sj = new StringJoiner(", ", "[", "]");
        forEach((k, v) -> {
            final StringBuilder sb = new StringBuilder()
                .append(k)
                .append('=');

            if (v instanceof CharSequence) {
                sb.append('"').append(v).append('"');
            } else if (v instanceof Proxy) {
                sb.append('"').append(v.getClass().getName()).append('"');
            } else {
                sb.append(v);
            }

            sj.add(sb);
        });

        return sj.toString();
    }
}
