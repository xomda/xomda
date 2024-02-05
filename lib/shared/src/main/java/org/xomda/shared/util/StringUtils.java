package org.xomda.shared.util;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringUtils {

	/**
	 * Turns "Some String" into "SomeString"
	 */
	public static String toPascalCase(final String in) {
		return null == in || in.isEmpty() ? in
				: Arrays.stream(in.split("\\s+")).map(String::toLowerCase).map(StringUtils::toUpper1)
				.collect(Collectors.joining());
	}

	/**
	 * Turns "Some String" into "someString"
	 */
	public static String toCamelCase(final String in) {
		return toLower1(toPascalCase(in));
	}

	/**
	 * Turns the first character of the given string to lowercase. That's it.
	 */
	public static String toLower1(final String in) {
		if (null == in || in.isEmpty())
			return in;
		return Character.toLowerCase(in.charAt(0)) + in.substring(1);
	}

	/**
	 * Turns the first character of the given string to UPPERCASE. That's it.
	 */
	public static String toUpper1(final String in) {
		if (null == in || in.isEmpty())
			return in;
		return Character.toUpperCase(in.charAt(0)) + in.substring(1);
	}

	public static boolean isNullOrEmpty(String str) {
		return null == str || str.isEmpty();
	}

	public static boolean isNullOrBlank(String str) {
		return null == str || str.isBlank();
	}

}
