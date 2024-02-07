package org.xomda.core.template.context;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.xomda.core.template.context.java.JavaTemplateContext;
import org.xomda.shared.exception.SneakyThrow;

public class WritableContextTest {

	@Test
	public void testSimple() {
		assertEquals("ok", withContext(ctx -> ctx.print("ok")));
		assertEquals("ok\n", withContext(ctx -> ctx.println("ok")));
		assertEquals("\n", withContext(WritableContext::println));
	}

	static String withContext(final Consumer<WritableContext<JavaTemplateContext>> supplier) {
		try (
				final ByteArrayOutputStream baos = new ByteArrayOutputStream();
				final BufferedOutputStream bos = new BufferedOutputStream(baos);
				final JavaTemplateContext context = JavaTemplateContext.create("com.example.Class", bos);
		) {
			supplier.accept(context);
			context.flush();
			return baos.toString();
		} catch (final IOException e) {
			SneakyThrow.throwSneaky(e);
			return null;
		}
	}

}
