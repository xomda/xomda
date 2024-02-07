package org.xomda.core.java;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

public class JavaImportServiceTest {

	private static final String TEST_CLASS = "com.example.TestClass";

	@Test
	public void testConstructor() {
		assertDoesNotThrow(() -> new JavaImportService(TEST_CLASS));
		assertDoesNotThrow(() -> new JavaImportService("a"));

		assertThrows(IllegalArgumentException.class, () -> new JavaImportService(null));
		assertThrows(IllegalArgumentException.class, () -> new JavaImportService(""));
		assertThrows(IllegalArgumentException.class, () -> new JavaImportService(" "));
		assertThrows(IllegalArgumentException.class, () -> new JavaImportService("**"));
	}

	@Test
	public void testAddImport() {
		JavaImportService service = new JavaImportService(TEST_CLASS);

		assertEquals("List", service.addImport("java.util.List"));
		assertEquals("java.util2.List", service.addImport("java.util2.List"));

		// classes which also exist in java.lang should be fully qualified
		assertEquals("java.not.lang.String", service.addImport("java.not.lang.String"));
		assertEquals("String", service.addImport("java.lang.String"));
		assertEquals("java.not.lang.either.String", service.addImport("java.not.lang.either.String"));

		assertEquals("org.test.Long", service.addImport("org.test.Long"));
		assertEquals("Long", service.addImport("java.lang.Long"));

		assertEquals("Example", service.addImport("com.example.Example"));
		assertEquals("com.example.other.Example", service.addImport("com.example.other.Example"));

		assertEquals("Example2", service.addImport("com.example.Example2"));
		assertEquals("com.example.other.Example2", service.addImport("com.example.other.Example2"));
	}

	@Test
	public void testAddStaticImport() {
		JavaImportService service = new JavaImportService(TEST_CLASS);
		assertEquals("of", service.addStaticImport("java.util.List.of"));
		assertEquals("of", service.addStaticImport("java.util.List.of"));
		assertEquals("of", service.addStaticImport(List.class, "of"));
		// of already exists
		assertEquals("com.example.of", service.addStaticImport("com.example.of"));
		assertEquals("org.junit.jupiter.api.Test.of", service.addStaticImport(Test.class, "of"));

		// crap is new
		assertEquals("crap", service.addStaticImport(Test.class, "crap"));

		// of already exists, so it's can't be imported as a class either
		assertEquals("com.example.of", service.addImport("com.example.of"));
	}

	@Test
	public void testAddGenericImport() {
		JavaImportService service = new JavaImportService(TEST_CLASS);
		assertEquals("List<TestClass>", service.addGenericImport("java.util.List", "com.example.TestClass"));
		assertEquals("List<?>", service.addGenericImport("java.util.List"));

		assertEquals("Test<?>", service.addGenericImport(Test.class, new String[0]));
		assertEquals("Test<?>", service.addGenericImport(Test.class, new Class[0]));

		assertEquals("Test<Object>", service.addGenericImport(Test.class, Object.class));
		assertEquals("Test<Object, Object, Object>", service.addGenericImport(Test.class, Object.class, Object.class, Object.class));
	}

}
