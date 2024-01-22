package org.xomda.core.java;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.xomda.shared.util.ReflectionUtils;

public class JavaImportService {

    private final Map<String, String> imports = new ConcurrentHashMap<>();
    private final String localClass;

    private final List<String> sortOrder = Arrays.asList("static java.", "static", "java.");

    public JavaImportService(String localClass) {
        if (!org.xomda.core.java.JavaUtils.isValidClassName(localClass)) {
            throw new IllegalArgumentException("Invalid class name:" + localClass);
        }
        this.localClass = localClass;
    }

    public String getLocalClassName() {
        return localClass;
    }

    public String addImport(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return addImport(ReflectionUtils
            .getBareType(clazz)
            .getName()
        );
    }

    public String addImport(String fullyQualifiedClassName) {
        return addImport(fullyQualifiedClassName, false);
    }

    public String addStaticImport(String methodName) {
        return addImport(methodName, true);
    }

    public String addStaticImport(Class<?> clazz, String methodName) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(methodName);
        return addStaticImport(
            ReflectionUtils
                .getBareType(clazz)
                .getName() + "." + methodName
        );
    }

    public String addImport(String fullyQualifiedClassName, boolean isStatic) {
        Objects.requireNonNull(fullyQualifiedClassName);

        if (!org.xomda.core.java.JavaUtils.hasPackage(fullyQualifiedClassName)) return fullyQualifiedClassName;

        String className = org.xomda.core.java.JavaUtils.getClassName(fullyQualifiedClassName);
        String registeredClassName = (isStatic ? "static " : "") + fullyQualifiedClassName;

        if (imports.containsKey(className)) {
            return imports.get(className).equals(fullyQualifiedClassName)
                ? className
                : fullyQualifiedClassName;
        }

        boolean isJavaLang = org.xomda.core.java.JavaUtils.isGlobal(fullyQualifiedClassName);
        boolean existsInJavaLang = existsInJavaLang(fullyQualifiedClassName);

        // Return full Class names which also exist in java.lang
        if (!isJavaLang && existsInJavaLang) {
            return fullyQualifiedClassName;
        }

        // don't import java.lang classes
        if (isJavaLang) {
            return className;
        }

        // Same package? Doesn't need to be imported
        if (isSamePackage(fullyQualifiedClassName)) {
            // still remember that we are using a class from the same package
            imports.put(className, registeredClassName);
            return className;
        }

        imports.put(className, registeredClassName);
        return className;
    }

    public List<String> getSortOrder() {
        return sortOrder;
    }

    boolean existsInJavaLang(String className) {
        return ReflectionUtils
            .findClass("java.lang." + org.xomda.core.java.JavaUtils.getClassName(className))
            .isPresent();
    }

    boolean isSamePackage(String className) {
        return org.xomda.core.java.JavaUtils.isSamePackage(getLocalClassName(), className);
    }

    private Comparator<String> getComparator() {
        return sortOrder.stream()
            .map(JavaImportService::compareBeginsWith)
            .reduce((a, b) -> 0, Comparator::thenComparing)
            .thenComparing(String::compareTo);
    }

    public Stream<String> stream() {
        return imports.values().stream()
            .filter(c -> !org.xomda.core.java.JavaUtils.isSamePackage(c, getLocalClassName()))
            .sorted(getComparator());
    }

    public void forEach(Consumer<String> consumer) {
        forEach(consumer, () -> { /* noop */ });
    }

    public void forEach(Consumer<String> consumer, Runnable newGroupHandler) {
        AtomicReference<String> lastRef = new AtomicReference<>();
        stream().forEach((String imp) -> {
            String last = lastRef.getAndUpdate(s -> imp);
            if (last != null && getSortOrder().stream()
                .anyMatch(s -> last.startsWith(s) && !imp.startsWith(s))
            ) {
                newGroupHandler.run();
            }
            consumer.accept(imp);
        });
    }

    public boolean isEmpty() {
        return imports.isEmpty();
    }

    private static Comparator<String> compareBeginsWith(final String startsWith) {
        return (a, b) -> {
            boolean isA = a.startsWith(startsWith);
            boolean isB = b.startsWith(startsWith);
            return isA
                ? isB ? a.compareTo(b) : -1
                : isB ? 1 : 0;
        };
    }

}
