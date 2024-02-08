package org.xomda.template.util;

import java.text.MessageFormat;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateFormat {

	// The regex pattern used to escape a format pattern.
	private static final Pattern rxBrackets = Pattern
			.compile(new StringJoiner("|", "(", ")").add("([\\{]+)(?!\\d+[^}]*\\})") // {{ -> {'{'
					.add("(?<!\\{\\d[^}]*)([\\}]+)") // }} -> }'}'
					.toString());

	/**
	 * The patterns we're going to provide will typically contain a lot of curly
	 * braces, which we don't want to start escaping all he time. This helper will
	 * perform that escaping for us.
	 */
	public static String escapePattern(final CharSequence pattern) {
		final Matcher matcher = rxBrackets.matcher(pattern.toString().replace("'", "''"));
		return matcher.replaceAll("'$1'");
	}

	/**
	 * Format a given text (pattern) which contains placeholders in the form of
	 * <code>{0}</code>, <code>{1}</code>, <code>{2}</code>, ... and replace those
	 * placeholders with their respective counterparts, found in the given
	 * "</code>args</code>" varargs parameter.
	 */
	public static CharSequence format(final CharSequence pattern, final Object... args) {
		return 0 == args.length ? pattern : MessageFormat.format(escapePattern(pattern), args);
	}

}
