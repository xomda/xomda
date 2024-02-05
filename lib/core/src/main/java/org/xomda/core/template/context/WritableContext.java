package org.xomda.core.template.context;

import static org.xomda.shared.util.ReflectionUtils.unchecked;

import java.io.PrintWriter;

import org.xomda.core.util.TemplateFormat;

public interface WritableContext<R extends WritableContext<R>> {

	PrintWriter getWriter();

	boolean isNewLine();

	void setNewLine(boolean state);

	static CharSequence format(final CharSequence pattern, final Object... args) {
		return TemplateFormat.format(pattern.toString(), args);
	}

	default R println() {
		getWriter().println();
		setNewLine(true);
		return unchecked(this);
	}

	default R println(final CharSequence text, final Object... args) {
		getWriter().println(format(text, args));
		setNewLine(true);
		return unchecked(this);
	}

	default R print(final CharSequence text, final Object... args) {
		getWriter().print(format(text, args));
		setNewLine(false);
		return unchecked(this);
	}

}
