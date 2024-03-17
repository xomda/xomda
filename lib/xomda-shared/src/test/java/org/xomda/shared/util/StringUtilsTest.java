package org.xomda.shared.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class StringUtilsTest {
	@Test
	void testToPascalCase() {
		// null stays null
		assertNull(StringUtils.toCamelCase(null));

		assertEquals("JorisAerts", StringUtils.toPascalCase("Joris Aerts"));
		assertEquals("Jorisaerts", StringUtils.toPascalCase("JorisAerts"));

		assertEquals("EMail", StringUtils.toPascalCase("E-Mail"));
	}

	@Test
	void testToCamelCase() {
		// null stays null
		assertNull(StringUtils.toCamelCase(null));

		assertEquals("jorisAerts", StringUtils.toCamelCase("Joris Aerts"));
		assertEquals("jorisaerts", StringUtils.toCamelCase("JorisAerts"));

		assertEquals("eMail", StringUtils.toCamelCase("E-Mail"));

	}

	@Test
	void toLower1() {
		// null stays null
		assertNull(StringUtils.toLower1(null));
		// zero characters
		assertEquals("", StringUtils.toLower1(""));
		// single character
		assertEquals("a", StringUtils.toLower1("a"));
		assertEquals("a", StringUtils.toLower1("A"));
		assertEquals("1", StringUtils.toLower1("1"));
		assertEquals("1a", StringUtils.toLower1("1a"));
		// multiple characters
		assertEquals("joris Aerts", StringUtils.toLower1("Joris Aerts"));
	}

	@Test
	void toUpper1() {
		// null stays null
		assertNull(StringUtils.toUpper1(null));
		// zero characters
		assertEquals("", StringUtils.toUpper1(""));
		// single character
		assertEquals("A", StringUtils.toUpper1("a"));
		assertEquals("A", StringUtils.toUpper1("A"));
		assertEquals("1", StringUtils.toUpper1("1"));
		assertEquals("1a", StringUtils.toUpper1("1a"));
		// multiple characters
		assertEquals("Joris aerts", StringUtils.toUpper1("joris aerts"));
	}

	@Test
	void testEscapeHTML() {
		// null stays null
		assertNull(StringUtils.escapeHTML(null));

		assertEquals("Joris Aerts", StringUtils.escapeHTML("Joris Aerts"));
		assertEquals("Joris Aerts &lt;&gt; &#129395;", StringUtils.escapeHTML("Joris Aerts <> 🥳"));
		assertEquals("&euro;", StringUtils.escapeHTML("€"));
		assertEquals("&#128128;", StringUtils.escapeHTML("💀"));
	}
}
