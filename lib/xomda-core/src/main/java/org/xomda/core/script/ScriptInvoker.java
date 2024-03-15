package org.xomda.core.script;

import java.util.Map;

@FunctionalInterface
public interface ScriptInvoker<T> {
	T invoke(Map<String, Object> scope);

	default T invoke() {
		return invoke(null);
	}
}

