package org.xomda.shared.util;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtils {

	private static final Pattern RX_ALLOWED_HTML_PUNCTATION = Pattern.compile("[.?!,\\[\\](){}]");
	private static final Predicate<String> ALLOWED_HTML_PUNCTATION = RX_ALLOWED_HTML_PUNCTATION.asMatchPredicate();

	/**
	 * Turns "Some String" into "SomeString"
	 */
	public static String toPascalCase(final String in) {
		return null == in || in.isEmpty()
				? in
				: Arrays
				.stream(in.split("\\s+"))
				.map(String::toLowerCase)
				.map(StringUtils::toUpper1)
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

	/**
	 * Escape text to conform HTML.
	 */
	public static String escapeHTML(CharSequence text) {
		if (null == text) return null;
		StringBuilder sb = new StringBuilder();
		int length = text.length();

		for (int i = 0; i < length; i++) {
			char c = text.charAt(i);
			if (Character.isAlphabetic(c)
					|| Character.isDigit(c)
					|| Character.isSpaceChar(c)
					|| ALLOWED_HTML_PUNCTATION.test("" + c)
			) {
				sb.append(c);
				continue;
			}

			sb.append("&");
			if (Character.isHighSurrogate(c) && i < length - 1) {
				sb.append('#').append(Character.toCodePoint(c, text.charAt(++i)));
			} else {
				sb.append(HTMLEntities.translate(c));
			}
			sb.append(';');

		}
		return sb.toString();
	}

}
