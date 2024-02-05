package org.xomda.core.template.context.java;

import java.util.regex.Pattern;

import org.xomda.core.template.context.DefaultContext;

public class TabbedContext extends DelegateContext<TabbedContext> implements DefaultContext<JavaTemplateContext> {

	private static final Pattern rxNewLines = Pattern.compile("([\\r\\n]+)");

	private final int tabCount;

	TabbedContext(final JavaTemplateContext parent, int tabCount) {
		super(parent);
		this.tabCount = tabCount;
	}

	@Override
	public int getTabCount() {
		return tabCount;
	}

	public String getTabs(int count) {
		return getSingleTab().repeat(count);
	}

	public String getTabs() {
		int tabCount = getTabCount();
		return tabCount < 1 ? "" : getTabs(tabCount);
	}

	public TabbedContext println() {
		return println("");
	}

	public TabbedContext println(CharSequence text, Object... args) {
		String tabs = getTabs();
		return super.println(tabs + process(text, tabs), args);
	}

	public TabbedContext print(CharSequence text, Object... args) {
		return super.print(process(text, getTabs()), args);
	}

	private static CharSequence process(CharSequence text, String tabs) {
		if (tabs.isEmpty())
			return text;
		return rxNewLines.matcher(text).replaceAll("$1" + tabs);
	}
}
