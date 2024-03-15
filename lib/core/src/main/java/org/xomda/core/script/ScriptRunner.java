package org.xomda.core.script;

import java.util.Map;
import java.util.function.Supplier;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptRunner {

	private static final ScriptEngineManager manager = new ScriptEngineManager();
	private static final ScriptEngine engine = manager.getEngineByName("js");

	public static CompiledScript compile(String script) throws ScriptException {
		return ((Compilable) engine).compile(script);
	}

	public static <T> Supplier<T> callable(String script) {
		try {
			CompiledScript compiled = compile(script);
			return (() -> {
				ScriptEngine engine = manager.getEngineByName("js");
				try {
					@SuppressWarnings("unchecked")
					T result = (T) compiled.eval(engine.getContext());
					return result;
				} catch (ScriptException e) {
					return null;
				}
			});
		} catch (ScriptException e) {
			return () -> null;
		}
	}

	public static <T> Supplier<T> callable(String script, Map<String, Object> bindings) {
		try {
			CompiledScript compiled = compile(script);
			Bindings bindingContext = engine.getBindings(ScriptContext.ENGINE_SCOPE);
			if (null != bindings) {
				bindingContext.putAll(bindings);
			}
			return (() -> {
				try {
					@SuppressWarnings("unchecked")
					T result = (T) compiled.eval(bindingContext);
					return result;
				} catch (ScriptException e) {
					return null;
				}
			});
		} catch (ScriptException e) {
			return () -> null;
		}
	}

}
