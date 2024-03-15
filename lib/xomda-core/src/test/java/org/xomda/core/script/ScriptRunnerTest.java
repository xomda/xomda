package org.xomda.core.script;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

public class ScriptRunnerTest {

	@Test
	public void testScript() {
		ScriptInvoker<String> invoker = ScriptRunner.parse("'Hello world!'");
		assertNotNull(invoker);
		assertEquals("Hello world!", invoker.invoke());
	}

	@Test
	public void testScriptWithBindings() {
		ScriptInvoker<String> test1 = ScriptRunner.parse("hello");
		assertNotNull(test1);
		assertEquals("Hello world!", test1.invoke(Map.of("hello", "Hello world!")));

		ScriptInvoker<String> test2 = ScriptRunner.parse("test()");
		assertNotNull(test2);
		assertEquals("Hello world!", test2.invoke(Map.of("test", (Supplier<String>) () -> "Hello world!")));

		// single quote
		ScriptInvoker<String> test3 = ScriptRunner.parse("test('Hello world!')");
		assertNotNull(test3);
		assertEquals("Hello world!", test3.invoke(Map.of("test", Function.identity())));

		// double quote
		ScriptInvoker<String> test4 = ScriptRunner.parse("test(\"Hello world!\")");
		assertNotNull(test4);
		assertEquals("Hello world!", test4.invoke(Map.of("test", Function.identity())));

		// backtick quote
		ScriptInvoker<String> test5 = ScriptRunner.parse("test(`Hello world!`)");
		assertNotNull(test5);
		assertEquals("Hello world!", test5.invoke(Map.of("test", Function.identity())));
	}

}
