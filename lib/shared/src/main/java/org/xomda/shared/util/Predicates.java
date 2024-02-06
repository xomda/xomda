package org.xomda.shared.util;

import java.util.function.Predicate;

public class Predicates {

	private static final Predicate<?> ALWAYS_TRUE = always(true);
	private static final Predicate<?> ALWAYS_FALSE = always(false);

	public static <T> Predicate<T> always(boolean result) {
		return (T value) -> result;
	}

	@SuppressWarnings("unchecked")
	public static <T> Predicate<T> alwaysTrue() {
		return (Predicate<T>) ALWAYS_TRUE;
	}

	@SuppressWarnings("unchecked")
	public static <T> Predicate<T> alwaysFalse() {
		return (Predicate<T>) ALWAYS_FALSE;
	}

}
