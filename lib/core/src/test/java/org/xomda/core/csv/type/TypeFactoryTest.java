package org.xomda.core.csv.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.format.FormatStyle;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.xomda.core.csv.type.parser.AbstractValueParserProvider;

class TypeFactoryTest {

	@Test
	void testString() {
		ValueParser parser = createFactory().getSetter(String.class);
		assertNull(parser.apply(null));
		assertNull(parser.apply(""));

		assertEquals("TRUE", parser.apply("TRUE"));
		assertEquals("a", parser.apply("a"));

		assertEquals(String.class, parser.apply("FALSE").getClass());
	}

	@Test
	void testBoolean() {
		ValueParser parser = createFactory().getSetter(Boolean.class);
		assertNull(parser.apply(""));
		assertNull(parser.apply(null));

		assertEquals(Boolean.TRUE, parser.apply("TRUE"));
		assertEquals(Boolean.TRUE, parser.apply("true"));
		assertEquals(Boolean.TRUE, parser.apply("True"));
		assertEquals(Boolean.TRUE, parser.apply("truE"));
		assertEquals(Boolean.FALSE, parser.apply("FALSE"));
		assertEquals(Boolean.FALSE, parser.apply("false"));
		assertEquals(Boolean.FALSE, parser.apply("False"));
		assertEquals(Boolean.FALSE, parser.apply("falsE"));

		assertEquals(Boolean.class, parser.apply("FALSE").getClass());
	}

	@Test
	void testPrimitiveBoolean() {
		ValueParser parser = createFactory().getSetter(boolean.class);
		assertFalse((boolean) parser.apply(""));
		assertFalse((boolean) parser.apply(null));

		assertTrue((boolean) parser.apply("TRUE"));
		assertTrue((boolean) parser.apply("true"));
		assertTrue((boolean) parser.apply("True"));
		assertTrue((boolean) parser.apply("truE"));

		assertFalse((boolean) parser.apply("FALSE"));
		assertFalse((boolean) parser.apply("false"));
		assertFalse((boolean) parser.apply("False"));
		assertFalse((boolean) parser.apply("falsE"));
	}

	@Test
	void testDouble() {
		ValueParser parser = createFactory().getSetter(Double.class);
		assertNull(parser.apply(""));
		assertNull(parser.apply(null));

		assertEquals(Double.class, parser.apply("2.0").getClass());
		assertEquals(Double.class, parser.apply("1").getClass());

		assertEquals(2.0d, parser.apply("2.0"));
		assertEquals(2.0d, parser.apply("2"));
	}

	@Test
	void testPrimitiveDouble() {
		ValueParser parser = createFactory().getSetter(double.class);
		assertEquals(Double.class, parser.apply("2.0").getClass());
		assertEquals(Double.class, parser.apply("1").getClass());
		assertEquals(0.0d, parser.apply(""));
		assertEquals(0.0d, parser.apply(null));
		assertEquals(2.0d, parser.apply("2.0"));
		assertEquals(2.0d, parser.apply("2"));
	}

	@Test
	void testEnum() {
		ValueParser parser = createFactory().getSetter(FormatStyle.class);
		assertNull(parser.apply(""));
		assertNull(parser.apply(null));
		assertEquals(FormatStyle.FULL, parser.apply("FULL"));
		assertEquals(FormatStyle.LONG, parser.apply("LONG"));
		assertEquals(FormatStyle.MEDIUM, parser.apply("MEDIUM"));

		assertEquals(FormatStyle.class, parser.apply("FULL").getClass());
	}

	@Test
	void testFloat() {
		ValueParser parser = createFactory().getSetter(Float.class);
		assertNull(parser.apply(""));
		assertNull(parser.apply(null));

		assertEquals(Float.class, parser.apply("2.0").getClass());
		assertEquals(Float.class, parser.apply("1").getClass());

		assertEquals(2.0f, parser.apply("2.0"));
		assertEquals(2.0f, parser.apply("2"));
	}

	@Test
	void testPrimitiveFloat() {
		ValueParser parser = createFactory().getSetter(float.class);
		assertEquals(Float.class, parser.apply("2.0").getClass());
		assertEquals(Float.class, parser.apply("1").getClass());
		assertEquals(0.0f, parser.apply(""));
		assertEquals(0.0f, parser.apply(null));
		assertEquals(2.0f, parser.apply("2.0"));
		assertEquals(2.0f, parser.apply("2"));
	}

	@Test
	void testInteger() {
		ValueParser parser = createFactory().getSetter(Integer.class);
		assertNull(parser.apply(""));
		assertNull(parser.apply(null));

		assertEquals(Integer.class, parser.apply("1").getClass());
		assertEquals(2, parser.apply("2"));

		assertThrowsExactly(NumberFormatException.class, () -> parser.apply("2.0"));
		assertThrowsExactly(NumberFormatException.class, () -> parser.apply("2.1"));
		assertThrowsExactly(NumberFormatException.class, () -> parser.apply("a"));
	}

	@Test
	void testPrimitiveInteger() {
		ValueParser parser = createFactory().getSetter(int.class);
		assertEquals(Integer.class, parser.apply("1").getClass());
		assertEquals(0, parser.apply(""));
		assertEquals(0, parser.apply(null));
		assertEquals(2, parser.apply("2"));

		assertThrowsExactly(NumberFormatException.class, () -> parser.apply("2.0"));
		assertThrowsExactly(NumberFormatException.class, () -> parser.apply("2.1"));
		assertThrowsExactly(NumberFormatException.class, () -> parser.apply("a"));
	}

	@Test
	void testLong() {
		ValueParser parser = createFactory().getSetter(Long.class);
		assertNull(parser.apply(""));
		assertNull(parser.apply(null));

		assertEquals(Long.class, parser.apply("1").getClass());
		assertEquals(2L, parser.apply("2"));

		assertThrowsExactly(NumberFormatException.class, () -> parser.apply("2.0"));
		assertThrowsExactly(NumberFormatException.class, () -> parser.apply("2.1"));
		assertThrowsExactly(NumberFormatException.class, () -> parser.apply("a"));
	}

	@Test
	void testPrimitiveLong() {
		ValueParser parser = createFactory().getSetter(long.class);
		assertEquals(Long.class, parser.apply("1").getClass());
		assertEquals(0L, parser.apply(""));
		assertEquals(0L, parser.apply(null));
		assertEquals(2L, parser.apply("2"));

		assertThrowsExactly(NumberFormatException.class, () -> parser.apply("2.0"));
		assertThrowsExactly(NumberFormatException.class, () -> parser.apply("2.1"));
		assertThrowsExactly(NumberFormatException.class, () -> parser.apply("a"));
	}

	record CustomType(String test) {
	}

	@Test
	void testCustomType() {
		AbstractValueParserProvider custom = new AbstractValueParserProvider(Predicate.isEqual(CustomType.class),
				CustomType::new) {
		};
		ValueParser parser = createFactory(custom).getSetter(CustomType.class);
		assertEquals(CustomType.class, parser.apply("1").getClass());
		assertEquals(new CustomType(""), parser.apply(""));
		assertEquals(new CustomType(null), parser.apply(null));
		assertEquals(new CustomType("2"), parser.apply("2"));
	}

	@Test
	void testUnknownType() {
		ValueParser parser = createFactory().getSetter(TypeFactoryTest.class);
		assertEquals(String.class, parser.apply("1").getClass());
	}

	private TypeFactory createFactory(AbstractValueParserProvider... parser) {
		TypeFactory factory = new TypeFactory();
		if (null != parser) {
			Stream.of(parser).forEach(factory::register);
		}
		return factory;
	}

}
