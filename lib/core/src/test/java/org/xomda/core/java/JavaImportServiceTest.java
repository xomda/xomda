package org.xomda.core.java;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class JavaImportServiceTest {

    @Test
    public void testConstructor() {
        assertDoesNotThrow(() -> new JavaImportService("com.example.TestClass"));
        assertDoesNotThrow(() -> new JavaImportService("a"));

        assertThrows(IllegalArgumentException.class, () -> new JavaImportService(null));
        assertThrows(IllegalArgumentException.class, () -> new JavaImportService(""));
        assertThrows(IllegalArgumentException.class, () -> new JavaImportService(" "));
        assertThrows(IllegalArgumentException.class, () -> new JavaImportService("**"));
    }

    @Test
    public void testAddImport() {
        JavaImportService service = new JavaImportService("com.example.TestClass");

        assertEquals("List", service.addImport("java.util.List"));
        assertEquals("java.util2.List", service.addImport("java.util2.List"));

        // classes which also exist in java.lang should be fully qualified
        assertEquals("java.not.lang.String", service.addImport("java.not.lang.String"));
        assertEquals("String", service.addImport("java.lang.String"));
        assertEquals("java.not.lang.either.String", service.addImport("java.not.lang.either.String"));

        assertEquals("org.test.Long", service.addImport("org.test.Long"));
        assertEquals("Long", service.addImport("java.lang.Long"));

        assertEquals("Example", service.addImport("com.example.Example"));
        assertEquals("com.example.other.Example", service.addImport("com.example.other.Example"));

        assertEquals("Example2", service.addImport("com.example.Example2"));
        assertEquals("com.example.other.Example2", service.addImport("com.example.other.Example2"));
    }

}
