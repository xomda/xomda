package org.xomda.core.java;

import java.io.Closeable;
import java.io.Flushable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.xomda.template.context.DefaultContext;

public class DeferredContext extends DelegateContext<DeferredContext> implements DefaultContext<JavaTemplateContext>, Flushable, Closeable {

	private final List<Runnable> actions = new ArrayList<>();

	DeferredContext(final JavaTemplateContext context) {
		super(context);
	}

	@Override
	public DeferredContext print(final CharSequence text, final Object... args) {
		actions.add(() -> super.print(text, args));
		return this;
	}

	@Override
	public DeferredContext println(final CharSequence text, final Object... args) {
		actions.add(() -> super.println(text, args));
		return this;
	}

	@Override
	public DeferredContext println() {
		actions.add(super::println);
		return this;
	}

	public boolean isEmpty() {
		return actions.isEmpty();
	}

	public void clear() {
		actions.clear();
	}

	@Override
	public void flush() {
		if (actions.isEmpty()) {
			return;
		}
		synchronized (actions) {
			final Iterator<Runnable> it = actions.iterator();
			while (it.hasNext()) {
				it.next().run();
				it.remove();
			}
		}
	}

}
