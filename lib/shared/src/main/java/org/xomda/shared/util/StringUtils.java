package org.xomda.shared.util;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtils {

	private static final Pattern RX_ALLOWED_PUNCTATION = Pattern.compile("[.?!,\\[\\](){}]");
	private static final Predicate<String> ALLOWED_PUNCTATION = RX_ALLOWED_PUNCTATION.asMatchPredicate();

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
		AtomicInteger highSurrogate = new AtomicInteger();
		text.chars().forEach((int c) -> {
			if (Character.isHighSurrogate((char) c)) {
				highSurrogate.set(c);
				return;
			}
			if (highSurrogate.get() > 0) {
				final int surrogate = highSurrogate.getAndSet(0);
				c = Character.toCodePoint((char) surrogate, (char) c);
			}
			if (Character.isAlphabetic(c)
					|| Character.isDigit(c)
					|| Character.isSpaceChar(c)
					|| ALLOWED_PUNCTATION.test("" + c)
			) {
				sb.append((char) c);
			} else {
				sb.append("&#").append(c).append(';');
			}
		});
		return sb.toString();
	}

}
