package org.xomda.shared.exception;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class SneakyThrow {

	public static <T, R, E extends Throwable> Function<T, R> sneaky(ThrowingFunction<T, R, E> fn) {
		return fn;
	}

	public static <T, E extends Throwable> Consumer<T> sneaky(ThrowingConsumer<T, E> c) {
		return c;
	}

	public static <T, U, E extends Throwable> BiConsumer<T, U> sneaky(ThrowingBiConsumer<T, U, E> c) {
		return c;
	}

	public static <E extends Throwable> void throwSneaky(Throwable e) throws E {
		@SuppressWarnings("unchecked")
		E genericException = (E) e;
		throw genericException;
	}

	@FunctionalInterface
	public interface ThrowingFunction<T, R, E extends Throwable> extends Function<T, R> {

		default R apply(T t) {
			try {
				return applyThrowing(t);
			} catch (Throwable e) {
				throwSneaky(e);
				return null;
			}
		}

		R applyThrowing(T t) throws E;

	}

	@FunctionalInterface
	public interface ThrowingConsumer<T, E extends Throwable> extends Consumer<T> {

		default void accept(T t) {
			try {
				acceptThrowing(t);
			} catch (Throwable e) {
				throwSneaky(e);
			}
		}

		void acceptThrowing(T t) throws E;

	}

	@FunctionalInterface
	public interface ThrowingBiConsumer<T, U, E extends Throwable> extends BiConsumer<T, U> {

		default void accept(T t, U u) {
			try {
				acceptThrowing(t, u);
			} catch (Throwable e) {
				throwSneaky(e);
			}
		}

		void acceptThrowing(T t, U u) throws E;

	}

}
