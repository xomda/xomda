package org.xomda.core.java.template;

import static org.xomda.shared.util.ReflectionUtils.unchecked;

import java.io.IOException;

abstract class DelegateContext<R extends DelegateContext<R>> extends JavaTemplateContext {

	private final JavaTemplateContext context;

	DelegateContext(final JavaTemplateContext context) {
		super(context);
		this.context = context;
	}

	@SuppressWarnings("resource")
	@Override
	public R print(final CharSequence text, final Object... args) {
		getContext().print(text, args);
		return unchecked(this);
	}

	@SuppressWarnings("resource")
	@Override
	public R println(final CharSequence text, final Object... args) {
		getContext().println(text, args);
		return unchecked(this);
	}

	@SuppressWarnings("resource")
	@Override
	public R println() {
		getContext().println();
		return unchecked(this);
	}

	protected JavaTemplateContext getContext() {
		return context;
	}

	@Override
	public boolean isNewLine() {
		return getContext().isNewLine();
	}

	@Override
	public void setNewLine(final boolean state) {
		getContext().setNewLine(state);
	}

	@Override
	public String getSingleTab() {
		return getContext().getSingleTab();
	}

	@Override
	public void setTabCharacter(final String character) {
		getContext().setTabCharacter(character);
	}

	@Override
	public void flush() throws IOException {
		getContext().flush();
	}

	@Override
	public void close() throws IOException {
		getContext().close();
	}
}
