package org.xomda.shared.util;

import java.util.function.Function;
import java.util.function.Supplier;

import org.xomda.shared.exception.SneakyThrow;

public class StreamUtils {

	public static <T, R, E extends Throwable> Function<T, R> mapOrGet(SneakyThrow.ThrowingFunction<T, R, E> fn, Supplier<R> defaultSupplier) {
		return (T o) -> {
			try {
				return fn.applyThrowing(o);
			} catch (Throwable e) {
				return defaultSupplier.get();
			}
		};
	}

	public static <T, R, E extends Throwable> Function<T, R> mapOr(SneakyThrow.ThrowingFunction<T, R, E> fn, R defaultValue) {
		return mapOrGet(fn, () -> null);
	}

	public static <T, R, E extends Throwable> Function<T, R> mapOrNull(SneakyThrow.ThrowingFunction<T, R, E> fn) {
		return mapOr(fn, null);
	}

}
