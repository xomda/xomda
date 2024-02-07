package org.xomda.shared.util;

import java.util.function.Function;
import java.util.function.Supplier;

import org.xomda.shared.exception.SneakyThrow;

/**
 * Utils that provide functionality for when working with {@link java.util.stream.Stream streams}.
 */
public class StreamUtils {

	public static <T, R, E extends Throwable> Function<T, R> mapOrGet(final SneakyThrow.ThrowingFunction<T, R, E> fn, final Supplier<R> defaultSupplier) {
		return (final T o) -> {
			try {
				return fn.applyThrowing(o);
			} catch (final Throwable e) {
				return defaultSupplier.get();
			}
		};
	}

	public static <T, R, E extends Throwable> Function<T, R> mapOr(final SneakyThrow.ThrowingFunction<T, R, E> fn, final R defaultValue) {
		return mapOrGet(fn, () -> null);
	}

	public static <T, R, E extends Throwable> Function<T, R> mapOrNull(final SneakyThrow.ThrowingFunction<T, R, E> fn) {
		return mapOr(fn, null);
	}

}
