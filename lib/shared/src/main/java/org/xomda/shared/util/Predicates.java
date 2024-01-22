package org.xomda.shared.util;

import java.util.function.Predicate;

public class Predicates {

    private static final Predicate<?> ALWAYS_TRUE = always(true);
    private static final Predicate<?> ALWAYS_FALSE = always(false);

    public static Predicate<?> always(boolean result) {
        return (Object value) -> result;
    }

    public static Predicate<?> alwaysTrue() {
        return ALWAYS_TRUE;
    }

    public static Predicate<?> alwaysFalse() {
        return ALWAYS_FALSE;
    }

}
