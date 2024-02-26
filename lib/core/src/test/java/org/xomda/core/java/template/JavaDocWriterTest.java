package org.xomda.core.java.template;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.xomda.core.java.JavaDocWriter;
import org.xomda.core.java.JavaTemplateContext;
import org.xomda.shared.exception.SneakyThrow;

public class JavaDocWriterTest {

	private static final String TAB = "    ";

	@Test
	public void test() {
		assertEquals(
				"""
						void test() {}

						/**
						 * Test 123
						 * Test 456
						 */
						void test() {}
						""",
				withContext(ctx -> ctx
						.println("void test() {}").println().withJavaDoc(doc -> {
							// make sure we got a JavaDocWriter
							assertInstanceOf(JavaDocWriter.class, doc);
							doc
									.println("Test 123")
									.println("Test 456");
						})
						.println("void test() {}")
				)
		);
	}

	@Test
	public void testWithTabs() {
		assertEquals(
				String.join(
						"\n",
						TAB.repeat(3) + "/**",
						TAB.repeat(3) + " * Test 123",
						TAB.repeat(3) + " * Test 456",
						TAB.repeat(3) + " */",
						""
				),
				withContext(ctx -> ctx.tab(tab1 -> tab1.tab(tab2 -> tab2.tab(tab3 -> tab3
						.withJavaDoc(doc -> doc
								// make sure we got a JavaDocWriter
								.println("Test 123")
								.println("Test 456")
						)
				))))
		);
	}

	@Test
	public void testLooselyWithTabs() {
		final int tabCount = 2;
		final String actual = withContext(ctx -> ctx.tab(tabCount, tab -> tab
				.withJavaDoc(doc -> {
					// make sure we got a JavaDocWriter
					assertInstanceOf(JavaDocWriter.class, doc);
					final JavaTemplateContext res = doc
							.println("Test 123")
							.println("Test 456");
					assertInstanceOf(JavaDocWriter.class, res);
				})
				.println("void test() {}")
		));
		assertNotNull(actual);
		actual.lines().forEach(line -> {
			assertTrue(line.startsWith(TAB.repeat(tabCount)), line);
		});
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

			final String actual = os.toString();
			assertNotNull(actual);

			return actual;
		} catch (final IOException e) {
			SneakyThrow.throwSneaky(e);
			return null;
		}
	}
}
