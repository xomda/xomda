package org.xomda.shared.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

class ReflectionUtilsTest implements ReflectionUtilsTestInterfaces {

    @Test
    void testExtendsFrom() {
        assertTrue(ReflectionUtils.extendsFrom(TestInterface1.class, ChildInterface1.class));
        assertTrue(ReflectionUtils.extendsFrom(TestInterface2.class, ChildInterface1.class));
        assertTrue(ReflectionUtils.extendsFrom(TestInterface2.class, ChildInterface2.class));

        assertFalse(ReflectionUtils.extendsFrom(ChildInterface1.class, TestInterface1.class));
        assertFalse(ReflectionUtils.extendsFrom(ChildInterface1.class, TestInterface2.class));
        assertFalse(ReflectionUtils.extendsFrom(ChildInterface2.class, TestInterface2.class));

        assertTrue(ReflectionUtils.extendsFrom(TestClass1.class, ChildClass1.class));
        assertTrue(ReflectionUtils.extendsFrom(TestClass2.class, ChildClass1.class));
        assertTrue(ReflectionUtils.extendsFrom(TestClass2.class, ChildClass2.class));

        assertFalse(ReflectionUtils.extendsFrom(ChildClass1.class, TestClass1.class));
        assertFalse(ReflectionUtils.extendsFrom(ChildClass1.class, TestClass2.class));
        assertFalse(ReflectionUtils.extendsFrom(ChildClass2.class, TestClass2.class));

        assertTrue(ReflectionUtils.extendsFrom(TestClass3.class, TestInterface1.class));
        assertTrue(ReflectionUtils.extendsFrom(TestClass3.class, ChildInterface1.class));

        assertTrue(ReflectionUtils.extendsFrom(TestClass4.class, TestInterface1.class));
        assertTrue(ReflectionUtils.extendsFrom(TestClass4.class, TestInterface2.class));
        assertTrue(ReflectionUtils.extendsFrom(TestClass4.class, TestInterface1.class));
        assertTrue(ReflectionUtils.extendsFrom(TestClass4.class, ChildInterface1.class));
        assertTrue(ReflectionUtils.extendsFrom(TestClass4.class, TestInterface1.class));
        assertTrue(ReflectionUtils.extendsFrom(TestClass4.class, ChildInterface2.class));

        assertTrue(ReflectionUtils.extendsFrom(TestClass5.class, TestInterface1.class));
        assertTrue(ReflectionUtils.extendsFrom(TestClass5.class, TestInterface2.class));
        assertTrue(ReflectionUtils.extendsFrom(TestClass5.class, TestInterface1.class));
        assertTrue(ReflectionUtils.extendsFrom(TestClass5.class, ChildInterface1.class));
        assertTrue(ReflectionUtils.extendsFrom(TestClass5.class, TestInterface1.class));
        assertTrue(ReflectionUtils.extendsFrom(TestClass5.class, ChildInterface2.class));
    }

    @Test
    @SuppressWarnings("rawtypes")
    void testExtendsFromPredicate() {
        final Predicate<Class> p1 = ReflectionUtils.extendsFrom(ChildInterface1.class);
        assertTrue(p1.test(TestInterface2.class));
        assertTrue(p1.test(TestInterface2.class));

        final Predicate<Class> p2 = ReflectionUtils.extendsFrom(TestInterface2.class);
        assertFalse(p2.test(ChildInterface1.class));
        assertFalse(p2.test(ChildInterface2.class));

        final Predicate<Class> p3 = ReflectionUtils.extendsFrom(ChildClass1.class);
        assertTrue(p3.test(TestClass1.class));
        assertTrue(p3.test(TestClass2.class));

        final Predicate<Class> p4 = ReflectionUtils.extendsFrom(TestClass1.class);
        assertFalse(p4.test(ChildClass1.class));
        assertFalse(p4.test(ChildClass2.class));
    }

}
