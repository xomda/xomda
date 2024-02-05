package org.xomda.core.template.context.java;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.xomda.shared.exception.SneakyThrow;

public class JavaDocWriterTest {

	private static final String TAB = "    ";

	@Test
	public void testJavaDoc() {
		String actual = withContext(ctx -> {
			ctx.println("void test() {}").println().addDocs(doc -> {
				// make sure we got a JavaDocWriter
				assertInstanceOf(JavaDocWriter.class, doc);
				doc.println("Test 123").println("Test 456");
			}).println("void test() {}");
		});

		assertNotNull(actual);
		assertEquals("""
				void test() {}

				/**
				 * Test 123
				 * Test 456
				 */
				void test() {}
				""", actual);
	}

	@Test
	public void testTabbedJavaDoc() {
		int tabCount = 2;
		String actual = withContext(ctx -> ctx.tab(tabCount, tab -> tab.addDocs(doc -> {
			// make sure we got a JavaDocWriter
			assertInstanceOf(JavaDocWriter.class, doc);
			JavaTemplateContext res = doc.println("Test 123").println("Test 456");
			assertInstanceOf(JavaDocWriter.class, res);
		}).println("void test() {}")));

		System.out.println(actual);

		assertNotNull(actual);
		actual.lines().forEach(line -> {
			assertTrue(line.startsWith(TAB.repeat(tabCount)), line);
		});

		assertNotNull(actual);
	}

	static String withContext(Consumer<JavaTemplateContext> supplier) {
		try (ByteArrayOutputStream os = new ByteArrayOutputStream();
				BufferedOutputStream bos = new BufferedOutputStream(os);
				JavaTemplateContext context = new JavaTemplateContext("com.example.com", bos)) {
			context.setTabCharacter(TAB);
			supplier.accept(context);
			context.flush();
			return os.toString();
		} catch (IOException e) {
			SneakyThrow.throwSneaky(e);
			return null;
		}
	}
}
