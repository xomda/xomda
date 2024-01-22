package org.xomda.shared.util;

interface ReflectionUtilsTestInterfaces {

    // test interfaces

    interface ChildInterface1 {
    }

    interface ChildInterface2 extends ChildInterface1 {
    }

    interface TestInterface1 extends ChildInterface1 {
    }

    interface TestInterface2 extends ChildInterface2 {
    }

    // test classes

    class ChildClass1 {
    }

    class ChildClass2 extends ChildClass1 {
    }

    class TestClass1 extends ChildClass1 {
    }

    class TestClass2 extends ChildClass2 {
    }

    class TestClass3 implements TestInterface1 {
    }

    class TestClass4 implements TestInterface1, TestInterface2 {
    }

    class TestClass5 extends TestClass4 {
    }

}
