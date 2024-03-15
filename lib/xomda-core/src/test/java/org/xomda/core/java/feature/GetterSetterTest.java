package org.xomda.core.java.feature;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.xomda.core.java.JavaTemplateContext;
import org.xomda.core.java.util.GetterSetter;
import org.xomda.shared.exception.SneakyThrow;

public class GetterSetterTest {

	private static final String TAB = "\t";

	@Test
	public void testInterface() {
		assertEquals("String getName();\n\nvoid setName(final String name);\n\n", withContext(
				GetterSetter.create("java.lang.String", "name")
						.declareOnly()
						.withModifiers(Modifier.PRIVATE)::writeTo
		));

		assertEquals(
				"""
						private String name;

						/**
						 * Test
						 */
						private String getName() {
						\treturn name;
						}

						""",
				withContext(
						GetterSetter.create("java.lang.String", "name")
								.withModifiers(Modifier.PRIVATE)
								.withJavaDoc("Test")
								.getOnly()::writeTo
				)
		);

		assertEquals(
				"""
						private String name;

						/**
						 * Test
						 */
						private void setName(final String name) {
						\tthis.name = name;
						}

						""",
				withContext(
						GetterSetter.create("java.lang.String", "name")
								.withModifiers(Modifier.PRIVATE)
								.withJavaDoc("Test")
								.setOnly()::writeTo
				)
		);

		assertEquals(
				"""
						private String name;

						/**
						 * Test
						 */
						private String getName() {
						\treturn name;
						}

						/**
						 * Test
						 */
						private void setName(final String name) {
						\tthis.name = name;
						}

						""",
				withContext(
						GetterSetter.create("java.lang.String", "name")
								.withModifiers(Modifier.PRIVATE)
								.withJavaDoc("Test")::writeTo
				)
		);

	}

	static String withContext(final Consumer<JavaTemplateContext> supplier) {
		try (
				final ByteArrayOutputStream os = new ByteArrayOutputStream();
				final BufferedOutputStream bos = new BufferedOutputStream(os);
				final JavaTemplateContext context = JavaTemplateContext.create("com.example.com", bos);
		) {
			context.setTabCharacter(TAB);
			supplier.accept(context);
			context.flush();
			return os.toString();
		} catch (final IOException e) {
			SneakyThrow.throwSneaky(e);
			return null;
		}
	}

}
