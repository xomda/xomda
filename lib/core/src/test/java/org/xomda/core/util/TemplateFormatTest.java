package org.xomda.core.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TemplateFormatTest {

    @Test
    public void testEscapeBrackets() {
        assertEquals("{0}", TemplateFormat.escapePattern("{0}"));
        assertEquals("'{'{0}'}'", TemplateFormat.escapePattern("{{0}}"));
        assertEquals("'{{'{0}'}}'", TemplateFormat.escapePattern("{{{0}}}"));
    }

    @Test
    public void testEscapeQuotes() {
        assertEquals("''ok''", TemplateFormat.escapePattern("'ok'"));
        assertEquals("\"ok\"", TemplateFormat.escapePattern("\"ok\""));
    }

    @Test
    public void testEscape() {
        assertEquals("public void {0} '{'test();'}'", TemplateFormat.escapePattern("public void {0} {test();}"));
        assertEquals("new {0}() '{{' '}}'", TemplateFormat.escapePattern("new {0}() {{ }}"));
        assertEquals("new {0}() '{' '{' '}' '}'", TemplateFormat.escapePattern("new {0}() { { } }"));
    }

    @Test
    public void testFormat() {
        assertEquals("Test Test", TemplateFormat.format("{0} Test", "Test"));
        assertEquals("public void testFormat {test();}", TemplateFormat.format("public void {0} {test();}", "testFormat"));
        assertEquals("new SomeAbstractClass() {{ }}", TemplateFormat.format("new {0}() {{ }}", "SomeAbstractClass"));
        assertEquals("new SomeAbstractClass() { { } }", TemplateFormat.format("new {0}() { { } }", "SomeAbstractClass"));

        assertEquals("print('a')", TemplateFormat.format("print('{0}')", "a"));
        assertEquals("print(\"aaaa\")", TemplateFormat.format("print(\"{0}\")", "aaaa"));
    }

}
