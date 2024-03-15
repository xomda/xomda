package org.xomda.core.script;

import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ScriptRunnerTest {

	@Test
	public void testScript() {
		Supplier<String> supplier = ScriptRunner.callable("return 'Hello world!'");
		Assertions.assertEquals("Hello world!", supplier.get());
	}

}
