package org.xomda.core.java;

import java.util.regex.Pattern;

import org.xomda.template.context.DefaultContext;

public class TabbedContext extends DelegateContext<TabbedContext> implements DefaultContext<JavaTemplateContext> {

	private static final Pattern rxNewLines = Pattern.compile("([\\r\\n]+)");

	private final int tabCount;

	TabbedContext(final JavaTemplateContext parent, final int tabCount) {
		super(parent);
		this.tabCount = tabCount;
	}

	@Override
	public int getTabCount() {
		return tabCount;
	}

	public String getTabs(final int count) {
		return getSingleTab().repeat(count);
	}

	public String getTabs() {
		final int tabCount = getTabCount();
		return tabCount < 1 ? "" : getTabs(tabCount);
	}

	@Override
	public TabbedContext println() {
		return println("");
	}

	@Override
	public TabbedContext println(final CharSequence text, final Object... args) {
		final String tabs = isNewLine() ? getTabs() : "";
		return super.println(tabs + process(text, tabs), args);
	}

	@Override
	public TabbedContext print(final CharSequence text, final Object... args) {
		final String tabs = isNewLine() ? getTabs() : "";
		return super.print(tabs + process(text, getTabs()), args);
	}

	private static CharSequence process(final CharSequence text, final String tabs) {
		if (tabs.isEmpty()) {
			return text;
		}
		return rxNewLines.matcher(text).replaceAll("$1" + tabs);
	}
}
