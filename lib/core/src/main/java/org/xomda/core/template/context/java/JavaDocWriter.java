package org.xomda.core.template.context.java;

import java.util.regex.Pattern;

public class JavaDocWriter extends DelegateContext<JavaDocWriter> implements AutoCloseable {

	private static final Pattern rxNewLine = Pattern.compile("([\\r\\n])");
	private static final String LINE_PREFIX = " * ";

	private boolean isOpen;

	JavaDocWriter(JavaTemplateContext parent) {
		super(parent);
	}

	@Override
	public JavaDocWriter print(final CharSequence text, final Object... args) {
		open();
		return super.print(process(text), args);
	}

	@Override
	public JavaDocWriter println(final CharSequence text, final Object... args) {
		open();
		return super.println(LINE_PREFIX + process(text), args);
	}

	@Override
	public JavaDocWriter println() {
		if (isNewLine())
			return println("");
		open();
		return super.println();
	}

	boolean isOpen() {
		return isOpen;
	}

	synchronized void open() {
		if (isOpen())
			return;
		isOpen = true;
		super.println("/**");
	}

	@Override
	public void close() {
		if (!isOpen())
			return;
		super.println(" */");
	}

	private static CharSequence process(CharSequence text) {
		return rxNewLine.matcher(text).replaceAll("$1" + LINE_PREFIX);
	}
}
