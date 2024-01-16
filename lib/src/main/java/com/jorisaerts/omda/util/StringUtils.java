package com.jorisaerts.omda.util;

import java.util.Arrays;
import java.util.stream.Collectors;

import static java.lang.Character.toLowerCase;
import static java.lang.Character.toUpperCase;

public class StringUtils {

    /**
     * Turns "Some String" into "SomeString"
     */
    public static String toPascalCase(final String in) {
        return Arrays
                .stream(in.split("\\s+"))
                .map(s -> toUpperCase(s.charAt(0)) + s.substring(1))
                .collect(Collectors.joining());
    }

    /**
     * Turns "Some String" into "someString"
     */
    public static String toCamelCase(final String in) {
        final String out = toPascalCase(in);
        return toLowerCase(out.charAt(0)) + out.substring(1);
    }

    public static String toLower1(final String in) {
        if (null == in) return null;
        if (in.length() == 1) return in.toLowerCase();
        return Character.toLowerCase(in.charAt(0)) + in.substring(1);
    }

}
