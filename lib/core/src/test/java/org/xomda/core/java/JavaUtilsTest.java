package org.xomda.core.java;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class JavaUtilsTest {

	@Test
	public void isIsReserved() {
		assertTrue(JavaUtils.isReserved("new"));
		assertTrue(JavaUtils.isReserved("case"));
		assertTrue(JavaUtils.isReserved("switch"));
		assertTrue(JavaUtils.isReserved("enum"));
		assertTrue(JavaUtils.isReserved("package"));

		assertTrue(JavaUtils.isReserved("FMT"));
		assertFalse(JavaUtils.isReserved("fmt"));

		assertFalse(JavaUtils.isReserved("NEW"));
		assertFalse(JavaUtils.isReserved("CASE"));
	}

	@Test
	public void testGetPackageName() {
		assertEquals("java.lang", JavaUtils.getPackageName("java.lang.String"));
		assertEquals("java.util.concurrent", JavaUtils.getPackageName(ConcurrentHashMap.class));
	}

	@Test
	public void testGetClassName() {
		assertEquals("String", JavaUtils.getClassName("java.lang.String"));
		assertEquals("J", JavaUtils.getClassName("a.b.c.d.e.f.g.h.i.J"));
		assertEquals("ConcurrentHashMap", JavaUtils.getClassName(ConcurrentHashMap.class));
	}

	@Test
	public void isSamePackage() {
		assertTrue(JavaUtils.isSamePackage("java.lang.Long", "java.lang.String"));
		assertTrue(JavaUtils.isSamePackage("java.util.List", "java.util.Date"));
		assertTrue(JavaUtils.isSamePackage(List.class, Date.class));
		assertTrue(JavaUtils.isSamePackage(List.class.getName(), Date.class));

		assertFalse(JavaUtils.isSamePackage(Long.class.getName(), Date.class));
		assertFalse(JavaUtils.isSamePackage(Integer.class.getName(), "java.util.Date"));
		assertFalse(JavaUtils.isSamePackage("java.lang.Long", "java.util.Date"));
	}

	@Test
	public void testIsGlobal() {
		assertTrue(JavaUtils.isGlobal("java.lang"));
		assertTrue(JavaUtils.isGlobal("java.lang.String"));
		assertFalse(JavaUtils.isGlobal(ConcurrentHashMap.class.getName()));

		assertTrue(JavaUtils.isGlobal(String.class));
		assertFalse(JavaUtils.isGlobal(ConcurrentHashMap.class));
	}

	@Test
	public void testIsFullyQualified() {
		assertTrue(JavaUtils.isFullyQualified("java.lang.String"));

		assertFalse(JavaUtils.isFullyQualified("String"));
		assertFalse(JavaUtils.isFullyQualified(""));
		assertFalse(JavaUtils.isFullyQualified(null));
	}

	@Test
	public void isValidClassName() {
		assertTrue(JavaUtils.isValidClassName("java.lang.Long"));
		assertFalse(JavaUtils.isValidClassName(null));
		assertFalse(JavaUtils.isValidClassName(""));
		assertFalse(JavaUtils.isValidClassName("/"));
		assertFalse(JavaUtils.isValidClassName("."));
		assertTrue(JavaUtils.isValidClassName("Test"));
		assertFalse(JavaUtils.isValidClassName("Should Fail"));
	}

	@Test
	public void testGetJavaFile() {
		assertEquals(Paths.get("java", "lang", "Long.java"), JavaUtils.toJavaPath("java.lang.Long"));
	}

	@ParameterizedTest
	@CsvSource({ "foo,FOO", "fOo,FOO", "f-o,FO", "鵺 BAR,鵺_BAR", "one9,ONE9", "9one,_9ONE", "$,$", "#,_", "#\",_" })
	void testToEnumValue_produceValidValues(String input, String expected) {
		assertEquals(expected, JavaUtils.toEnumValue(input));
	}

}
