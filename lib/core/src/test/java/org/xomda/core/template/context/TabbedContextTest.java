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
        assertEquals("ok", withContext(1, ctx -> ctx.print("ok")));
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

    static String withContext(int tabCount, Consumer<TabbableContext<JavaTemplateContext>> supplier) {
        try (
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            JavaTemplateContext context = new JavaTemplateContext("com.example.Class", bos).tab(tabCount)
        ) {
            supplier.accept(context);
            context.flush();
            return os.toString();
        } catch (IOException e) {
            SneakyThrow.throwSneaky(e);
            return null;
        }
    }

}
