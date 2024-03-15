package org.xomda.core.script;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
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
		Supplier<String> supplier = ScriptRunner.callable("hello", Map.of("hello", "Hello world!"));
		assertEquals("Hello world!", supplier.get());
	}

}
