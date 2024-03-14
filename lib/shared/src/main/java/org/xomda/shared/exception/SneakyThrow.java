package org.xomda.shared.exception;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Turns regular java functions into throwing ones. They will still throw the original exception,
 * only will it be disguised as a Runtime Exception for the JVM.
 * <p>
 * This comes handy when working with {@link java.util.stream.Stream streams}
 * or {@link java.util.Optional optionals} for example.
 */
public class SneakyThrow {

	/**
	 * Turns the provided {@link ThrowingConsumer throwing Consumer} into a sneaky-throwing {@link Consumer}.
	 */
	public static <T, E extends Throwable> Consumer<T> sneaky(final ThrowingConsumer<T, E> c) {
		return c;
	}

	/**
	 * Turns the provided {@link ThrowingBiConsumer throwing BiConsumer} into a sneaky-throwing {@link BiConsumer}.
	 */
	public static <T, U, E extends Throwable> BiConsumer<T, U> sneaky(final ThrowingBiConsumer<T, U, E> c) {
		return c;
	}

	/**
	 * Turns the provided {@link ThrowingSupplier throwing Supplier} into a sneaky-throwing {@link Supplier}.
	 */
	public static <T, E extends Throwable> Supplier<T> sneaky(final ThrowingSupplier<T, E> c) {
		return c;
	}

	/**
	 * Turns the provided {@link ThrowingFunction throwing Function} into a sneaky-throwing {@link Function}.
	 */
	public static <T, R, E extends Throwable> Function<T, R> sneaky(final ThrowingFunction<T, R, E> fn) {
		return fn;
	}

	// use the methods below when ambiguous

	/**
	 * @see #sneaky(ThrowingConsumer)
	 */
	public static <T, E extends Throwable> Consumer<T> sneakyConsumer(final ThrowingConsumer<T, E> c) {
		return c;
	}

	/**
	 * @see #sneaky(ThrowingFunction)
	 */
	public static <T, R, E extends Throwable> Function<T, R> sneakyFunction(final ThrowingFunction<T, R, E> fn) {
		return fn;
	}

	/**
	 * @see #sneaky(ThrowingFunction)
	 */
	public static <T, U, R, E extends Throwable> BiFunction<T, U, R> sneakyBiFunction(final ThrowingBiFunction<T, U, R, E> fn) {
		return fn;
	}

	/**
	 * @see #sneaky(ThrowingSupplier)
	 */
	public static <T, E extends Throwable> Supplier<T> sneakySupplier(final ThrowingSupplier<T, E> c) {
		return c;
	}

	/**
	 * Turns the provided {@link ThrowinPredicate throwing Predicate} into a sneaky-throwing {@link Predicate}.
	 */
	public static <T, E extends Throwable> Predicate<T> sneakyPredicate(final ThrowinPredicate<T, E> p) {
		return p;
	}

	/**
	 * Throws a given {@link Exception checked Exception} as a generic.
	 * This way the given {@link Exception checked Exception} will be treated as
	 * if it's a {@link RuntimeException runtime Exception}, while still being the original one.
	 */
	public static <E extends Throwable> void throwSneaky(final Throwable e) throws E {
		@SuppressWarnings("unchecked")
		final E genericException = (E) e;
		throw genericException;
	}

	/**
	 * A {@link Function} which allows throwing.
	 */
	@FunctionalInterface
	public interface ThrowingFunction<T, R, E extends Throwable> extends Function<T, R> {

		@Override
		default R apply(final T t) {
			try {
				return applyThrowing(t);
			} catch (final Throwable e) {
				throwSneaky(e);
				return null; // "unreachable"
			}
		}

		R applyThrowing(T t) throws E;
	}

	/**
	 * A {@link BiFunction} which allows throwing.
	 */
	@FunctionalInterface
	public interface ThrowingBiFunction<T, U, R, E extends Throwable> extends BiFunction<T, U, R> {

		@Override
		default R apply(final T t, U u) {
			try {
				return applyThrowing(t, u);
			} catch (final Throwable e) {
				throwSneaky(e);
				return null; // "unreachable"
			}
		}

		R applyThrowing(T t, U u) throws E;
	}

	/**
	 * A {@link Consumer} which allows throwing.
	 */
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

	/**
	 * A {@link BiConsumer} which allows throwing.
	 */
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

	/**
	 * A {@link Supplier} which allows throwing.
	 */
	@FunctionalInterface
	public interface ThrowingSupplier<T, E extends Throwable> extends Supplier<T> {

		@Override
		default T get() {
			try {
				return getThrowing();
			} catch (final Throwable e) {
				throwSneaky(e);
				return null; // "unreachable"
			}
		}

		T getThrowing() throws E;
	}

	/**
	 * A {@link Predicate} which allows throwing.
	 */
	@FunctionalInterface
	public interface ThrowinPredicate<T, E extends Throwable> extends Predicate<T> {

		@Override
		default boolean test(T value) {
			try {
				return testThrowing(value);
			} catch (final Throwable e) {
				throwSneaky(e);
				return false; // "unreachable"
			}
		}

		boolean testThrowing(T value) throws E;
	}

}
