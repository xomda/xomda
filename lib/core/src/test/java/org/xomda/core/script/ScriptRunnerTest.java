package org.xomda.core.script;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

public class ScriptRunnerTest {

	@Test
	public void testScript() {
		Supplier<String> supplier = ScriptRunner.callable("'Hello world!'");
		assertEquals("Hello world!", supplier.get());
	}

	@Test
	public void testScriptWithBindings() {
		Supplier<String> test1 = ScriptRunner.callable("hello", Map.of("hello", "Hello world!"));
		assertEquals("Hello world!", test1.get());

		Supplier<String> test2 = ScriptRunner.callable("test()", Map.of("test", (Supplier<String>) () -> "Hello world!"));
		assertEquals("Hello world!", test2.get());

		// single quote
		Supplier<String> test3 = ScriptRunner.callable("test('Hello world!')", Map.of("test", Function.identity()));
		assertEquals("Hello world!", test3.get());

		// double quote
		Supplier<String> test4 = ScriptRunner.callable("test(\"Hello world!\")", Map.of("test", Function.identity()));
		assertEquals("Hello world!", test4.get());

		// backtick quote
		Supplier<String> test5 = ScriptRunner.callable("test(`Hello world!`)", Map.of("test", Function.identity()));
		assertEquals("Hello world!", test5.get());
	}

}
