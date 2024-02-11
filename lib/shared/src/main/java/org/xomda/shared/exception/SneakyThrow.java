package org.xomda.shared.exception;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Turns regular java functions into throwing ones. They will still throw the original exception,
 * only will it be disguised as a Runtime Exception for the JVM.
 * <p>
 * This comes handy when working with {@link java.util.stream.Stream streams}
 * or {@link java.util.Optional optionals} for example.
 */
public class SneakyThrow {

	public static <T, R, E extends Throwable> Function<T, R> sneaky(final ThrowingFunction<T, R, E> fn) {
		return fn;
	}

	public static <T, E extends Throwable> Consumer<T> sneaky(final ThrowingConsumer<T, E> c) {
		return c;
	}

	public static <T, U, E extends Throwable> BiConsumer<T, U> sneaky(final ThrowingBiConsumer<T, U, E> c) {
		return c;
	}

	public static <T, E extends Throwable> Supplier<T> sneaky(final ThrowingSupplier<T, E> c) {
		return c;
	}

	/**
	 * Throws a given {@link Exception Checked Exception} as a generic.
	 * This way the given {@link Exception Checked Exception} will be treated as
	 * if it's a {@link RuntimeException Runtime Exception}, while still being the original one.
	 */
	public static <E extends Throwable> void throwSneaky(final Throwable e) throws E {
		@SuppressWarnings("unchecked")
		final E genericException = (E) e;
		throw genericException;
	}

	@FunctionalInterface
	public interface ThrowingFunction<T, R, E extends Throwable> extends Function<T, R> {

		@Override
		default R apply(final T t) {
			try {
				return applyThrowing(t);
			} catch (final Throwable e) {
				throwSneaky(e);
				return null;
			}
		}

		R applyThrowing(T t) throws E;

	}

	@FunctionalInterface
	public interface ThrowingConsumer<T, E extends Throwable> extends Consumer<T> {

		@Override
		default void accept(final T t) {
			try {
				acceptThrowing(t);
			} catch (final Throwable e) {
				throwSneaky(e);
			}
		}

		void acceptThrowing(T t) throws E;

	}

	@FunctionalInterface
	public interface ThrowingBiConsumer<T, U, E extends Throwable> extends BiConsumer<T, U> {

		@Override
		default void accept(final T t, final U u) {
			try {
				acceptThrowing(t, u);
			} catch (final Throwable e) {
				throwSneaky(e);
			}
		}

		void acceptThrowing(T t, U u) throws E;

	}

	@FunctionalInterface
	public interface ThrowingSupplier<T, E extends Throwable> extends Supplier<T> {

		@Override
		default T get() {
			try {
				return getThrowing();
			} catch (final Throwable e) {
				throwSneaky(e);
				return null;
			}
		}

		T getThrowing() throws E;

	}

}
