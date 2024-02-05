package org.xomda.core.template.context;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.xomda.core.template.context.java.JavaTemplateContext;
import org.xomda.shared.exception.SneakyThrow;

public class TabbedContextTest {

	@Test
	public void testPrint() {
		// print shouldn't do anything
		assertEquals("  ok", withContext(1, ctx -> ctx.print("ok")));
	}

	@Test
	public void testPrintln() {
		// print shouldn't do anything
		assertEquals("  ok\n", withContext(1, ctx -> ctx.println("ok")));
	}

	@Test
	public void testNewLines() {
		assertEquals("  ok\n  ok\n", withContext(1, ctx -> ctx.println("ok\nok")));
		assertEquals("  \n", withContext(1, TabbableContext::println));

		assertEquals("  ok\r\n  ok\n", withContext(1, ctx -> ctx.println("ok\r\nok")));
		assertEquals("  ok\r\n\r\n  ok\n", withContext(1, ctx -> ctx.println("ok\r\n\r\nok")));

		assertEquals("    ok\n\n\n\n\n    ok\n", withContext(2, ctx -> ctx.println("ok\n\n\n\n\nok")));
	}

	/**
	 * Test if tabs are correctly placed when a line didn't end yet, but does in the following call.
	 * Because tabs are only placed after a newline.
	 */
	@Test
	public void testContinuation() {
		assertEquals("  ok", withContext(1, ctx -> ctx.print("ok")));
		assertEquals("  okok", withContext(1, ctx -> ctx
				.print("ok")
				.print("ok")
		));
		assertEquals("  ok\n  ok", withContext(1, ctx -> ctx
				.println("ok")
				.print("ok")
		));
		assertEquals("  ok\n  okok\n  ok", withContext(1, ctx -> ctx
				.println("ok")
				.print("ok")
				.println("ok")
				.print("ok")
		));

	}

	static String withContext(final int tabCount, final Consumer<TabbableContext<JavaTemplateContext>> supplier) {
		try (
				final ByteArrayOutputStream os = new ByteArrayOutputStream();
				final BufferedOutputStream bos = new BufferedOutputStream(os);
				final JavaTemplateContext context = JavaTemplateContext
						.create("com.example.Class", bos)
						.tab(tabCount);
		) {
			context.setTabCharacter("  ");
			supplier.accept(context);
			context.flush();
			return os.toString();
		} catch (final IOException e) {
			SneakyThrow.throwSneaky(e);
			return null;
		}
	}

}
