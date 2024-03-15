package org.xomda.core.java;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Set;

public class JavaUtils {

	private static final Collection<String> JAVA_RESERVED_KEYWORDS = Set.of("abstract", "continue", "for", "new",
			"switch", "assert", "default", "goto", "package", "synchronized", "boolean", "do", "if", "private", "this",
			"break", "double", "implements", "public", "throw", "byte", "else", "import", "throws", "case", "enum",
			"instanceof", "return", "transient", "catch", "extends", "int", "short", "try", "char", "final",
			"interface", "static", "void", "class", "finally", "long", "strictfp", "volatile", "const", "float",
			"native", "super", "while"
	);

	private static final Collection<String> JAVA_RESERVED_IDENTIFIER = Set.of("exports", "module", "non-sealed", "open",
			"opens", "permits", "provides", "record", "requires", "sealed", "to", "transitive", "uses", "var", "when",
			"with", "yield"
	);

	private static final Collection<String> JAVA_RESERVED_LITERALS = Set.of("true", "false", "null");

	private static final Collection<String> JAVA_RESERVED_CONSTANTS = Set.of("STR", "FMT", "RAW");

	/**
	 * Checks if the given "word" is reserved in the Java Language (ie.
	 * <code>if</code>, <code>then</code>, <code>else</code>, ...). If so, it should
	 * obviously not be used as identifier in the generated code, for example.
	 */
	public static boolean isReserved(final String word) {
		return JAVA_RESERVED_LITERALS.contains(word) || JAVA_RESERVED_IDENTIFIER.contains(word)
				|| JAVA_RESERVED_KEYWORDS.contains(word) || JAVA_RESERVED_CONSTANTS.contains(word);
	}

	public static String toIdentifier(final String in) {
		return !isReserved(in) ? in : toIdentifier(in.charAt(0) + in.substring(1).replaceAll("[aeiuop]+", ""));
	}

	public static <A, B> boolean isSamePackage(final Class<A> a, final Class<B> b) {
		return hasPackage(a) == hasPackage(b) && getPackageName(a).equals(getPackageName(b));
	}

	public static boolean isSamePackage(final String a, final String b) {
		return hasPackage(a) == hasPackage(b) && getPackageName(a).equals(getPackageName(b));
	}

	public static <T> boolean isSamePackage(final String a, final Class<T> b) {
		return hasPackage(a) == hasPackage(b) && getPackageName(a).equals(getPackageName(b));
	}

	public static boolean isFullyQualified(final String className) {
		return isValidClassName(className) && className.indexOf('.') > -1;
	}

	public static boolean hasPackage(final String className) {
		return isFullyQualified(className);
	}

	public static <T> boolean hasPackage(final Class<T> clazz) {
		return !clazz.getPackageName().isEmpty();
	}

	public static String getPackageName(final String fullyQualifiedClassName) {
		if (!hasPackage(fullyQualifiedClassName)) {
			return "";
		}
		return fullyQualifiedClassName.substring(0, fullyQualifiedClassName.lastIndexOf('.'));
	}

	public static <T> String getPackageName(final Class<T> clazz) {
		return getPackageName(getFullyQualifiedClassName(clazz));
	}

	public static String getClassName(final String fullyQualifiedClassName) {
		if (!hasPackage(fullyQualifiedClassName)) {
			return fullyQualifiedClassName;
		}
		return fullyQualifiedClassName.substring(fullyQualifiedClassName.lastIndexOf('.') + 1);
	}

	public static <T> String getClassName(final Class<T> clazz) {
		return clazz.getSimpleName();
	}

	public static boolean isValidPackageName(final String packageName) {
		return isValidClassName(packageName);
	}

	public static boolean isValidClassName(final String className) {
		return null != className && !className.isBlank() && !className.contains(" ")
				&& className.replace(".", "").matches("[a-zA-Z0-9_.]+");
	}

	@SuppressWarnings("serial")
	private static class IllegalClassNameException extends IllegalArgumentException {
		IllegalClassNameException(final String className) {
			super("Invalid class name:" + className);
		}
	}

	public static void validateClassName(final String className, final boolean withPackage) {
		final boolean validPackage = withPackage && hasPackage(className)
				&& isValidPackageName(getPackageName(className));
		final boolean validClassName = isValidClassName(getClassName(className));
		if (!validPackage || !validClassName) {
			throw new IllegalClassNameException(className);
		}
	}

	public static void validateClassName(final String className) {
		validateClassName(className, false);
	}

	public static boolean isGlobal(final String className) {
		return hasPackage(className) && ("java.lang".equals(className)
				|| className.startsWith("java.lang.") && className.lastIndexOf('.') == 9);
	}

	public static <T> boolean isGlobal(final Class<T> clazz) {
		return null != clazz && "java.lang".equals(clazz.getPackageName());
	}

	public static <T> String getFullyQualifiedClassName(final Class<T> clazz) {
		return clazz.getName();
	}

	public static Path toJavaPath(final String className) {
		return Paths.get(className.replaceAll("\\.", File.separator) + ".java");

	}

}
