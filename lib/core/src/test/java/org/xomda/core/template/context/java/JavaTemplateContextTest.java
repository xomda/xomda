package org.xomda.core.template.context.java;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.xomda.core.template.context.WritableContext;
import org.xomda.shared.exception.SneakyThrow;

public class JavaTemplateContextTest {

	@Test
	public void test() {
		assertEquals("ok", withContext(ctx -> ctx.print("ok")));
		assertEquals("ok\n", withContext(ctx -> ctx.println("ok")));
		assertEquals("\n", withContext(WritableContext::println));
	}

	@Test
	public void testTabs() {
		// tab consumer
		assertEquals("  ok\n", withContext(ctx -> ctx.tab(c -> c.println("ok"))));
		assertEquals("    ok\n", withContext(ctx -> ctx.tab(2, c -> c.println("ok"))));
		// negative tabs
		assertEquals("ok\n", withContext(ctx -> ctx.tab(-10, c -> c.println("ok"))));
	}

	@Test
	public void testInlineTabs() {
		assertEquals("  ok\n", withContext(ctx -> ctx.tab().println("ok")));
		assertEquals("    ok\n", withContext(ctx -> ctx.tab(2).println("ok")));
		assertEquals("      ok\n", withContext(ctx -> ctx.tab().tab().tab().println("ok")));
	}

	@Test
	public void testMultiTabs() {
		// tab consumer
		assertEquals("    ok\n", withContext(ctx -> ctx.tab(ctx2 -> ctx2.tab(ctx3 -> ctx3.println("ok")))));
	}

	@Test
	public void testCustomTabs() {
		// tab consumer
		assertEquals("\t\tok\n", withContext("\t", ctx -> ctx.tab(ctx2 -> ctx2.tab(ctx3 -> ctx3.println("ok")))));
	}

	@Test
	public void testImports() {
		withContext("\t", ctx -> {
			ctx.addImport(Class.class);
			ctx.addImport(Integer.class);
			ctx.addImport(Long.class);
			ctx.addImport(String.class);
			ctx.addImport(Date.class);
			ctx.addImport("com.example.TestClass");
			ctx.addImport("com.example.TestClass");
			ctx.addImport("java.lang.ThisClassDoesNotExist");
			ctx.addStaticImport("java.lang.ThisClassDoesNotExist.someMethod");
			ctx.addStaticImport(Long.class, "parseLong");
			ctx.addImport("some.other.Clazz");
			ctx.addStaticImport("some.other.Clazz.withSomeOtherMethod");
			ctx.addImport("zzz.zzz.ZZZ");

			final List<String> imports = ctx.getImports();

			assertFalse(imports.isEmpty());
			assertEquals(9, imports.size());
			assertEquals("import static java.lang.Long.parseLong;", imports.get(0));
			assertEquals("import zzz.zzz.ZZZ;", imports.get(imports.size() - 1));
		});
	}

	static String withContext(final Consumer<JavaTemplateContext> supplier) {
		return withContext("  ", supplier);
	}

	static String withContext(final String tabCharacter, final Consumer<JavaTemplateContext> supplier) {
		try (
				final ByteArrayOutputStream os = new ByteArrayOutputStream();
				final BufferedOutputStream bos = new BufferedOutputStream(os);
				final JavaTemplateContext context = JavaTemplateContext.create("com.example.com", bos);
		) {
			context.setTabCharacter(tabCharacter);
			supplier.accept(context);
			context.flush();
			return os.toString();
		} catch (final IOException e) {
			SneakyThrow.throwSneaky(e);
			return null;
		}
	}

}
