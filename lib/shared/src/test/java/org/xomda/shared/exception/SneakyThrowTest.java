package org.xomda.shared.exception;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.xomda.shared.exception.SneakyThrow.sneaky;
import static org.xomda.shared.exception.SneakyThrow.throwSneaky;

import java.io.IOException;
import java.text.ParseException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.xomda.shared.exception.SneakyThrow.ThrowingConsumer;
import org.xomda.shared.exception.SneakyThrow.ThrowingFunction;

public class SneakyThrowTest {

	static class TestCheckedException extends Exception {
		private static final long serialVersionUID = -3506879281736101983L;
	}

	@Test
	public void testThrowSneaky() {
		assertThrowsExactly(TestCheckedException.class, () -> throwSneaky(new TestCheckedException()));
		assertThrowsExactly(IOException.class, () -> throwSneaky(new IOException()));
	}

	@Test
	public void testFunction() {
		assertThrowsExactly(TestCheckedException.class, () -> map(sneaky(SneakyThrowTest::throwTestCheckedException)));
		assertThrowsExactly(
				ParseException.class,
				() -> map(sneaky((ThrowingFunction<Object, Object, Exception>) (o -> {
					throw new ParseException("bla", 0);
				})))
		);
	}

	@Test
	public void testConsumer() {
		assertThrowsExactly(TestCheckedException.class, () -> consume(sneaky((ThrowingConsumer<Object, ? extends Throwable>) o -> throwTestCheckedException(null))));
	}

	static Object throwTestCheckedException(final Object args) throws TestCheckedException {
		throw new TestCheckedException();
	}

	static void consume(final Consumer<Object> consumer) {
		consumer.accept(new Object());
	}

	static void supply(final Supplier<Object> supplier) {
		supplier.get();
	}

	static void map(final Function<Object, Object> fn) {
		fn.apply(new Object());
	}

}
