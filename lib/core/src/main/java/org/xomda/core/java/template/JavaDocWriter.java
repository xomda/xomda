package org.xomda.core.java.template;

import java.util.regex.Pattern;

import org.xomda.shared.util.StringUtils;

public class JavaDocWriter extends DelegateContext<JavaDocWriter> implements AutoCloseable {

	private static final Pattern rxNewLine = Pattern.compile("([\\r\\n])");

	private static final String LINE_PREFIX = " * ";
	private static final String DOC_OPEN_TAG = "/**";
	private static final String DOC_CLOSE_TAG = " */";

	private boolean isOpen;

	JavaDocWriter(final JavaTemplateContext parent) {
		super(parent);
	}

	@Override
	public JavaDocWriter print(final CharSequence text, final Object... args) {
		open();
		return super.print(process(text), args);
	}

	public JavaDocWriter printEscaped(final CharSequence text, final Object... args) {
		return print(StringUtils.escapeHTML(text), args);
	}

	@Override
	public JavaDocWriter println(final CharSequence text, final Object... args) {
		open();
		return super.println(LINE_PREFIX + process(text), args);
	}

	public JavaDocWriter printlnEscaped(final CharSequence text, final Object... args) {
		return println(StringUtils.escapeHTML(text), args);
	}

	@Override
	public JavaDocWriter println() {
		if (isNewLine()) {
			return println("");
		}
		open();
		return super.println();
	}

	boolean isOpen() {
		return isOpen;
	}

	synchronized void open() {
		if (isOpen()) {
			return;
		}
		isOpen = true;
		super.println(DOC_OPEN_TAG);
	}

	@Override
	public void close() {
		if (!isOpen()) {
			return;
		}
		super.println(DOC_CLOSE_TAG);
	}

	private static CharSequence process(final CharSequence text) {
		return rxNewLine.matcher(text).replaceAll("$1" + LINE_PREFIX);
	}
}
