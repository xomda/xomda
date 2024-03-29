package org.xomda.template.context;

import static org.xomda.shared.util.ReflectionUtils.unchecked;

import java.util.function.Consumer;

public interface TabbableContext<R extends TabbableContext<R>> extends WritableContext<R> {

	String DEFAULT_TAB_CHARACTER = "\t";

	default R tab() {
		return tab(1);
	}

	R tab(int count);

	default R tab(final Consumer<R> consumer) {
		return tab(1, consumer);
	}

	default R tab(final int count, final Consumer<R> consumer) {
		consumer.accept(tab(count));
		return unchecked(this);
	}

}
