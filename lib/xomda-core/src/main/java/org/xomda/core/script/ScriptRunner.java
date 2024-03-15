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

import org.slf4j.Logger;
import org.xomda.shared.logging.LogService;

public class ScriptRunner {

	private static final String JS_ENGINE = "js";
	private static final ScriptEngineManager manager;
	private static final ScriptEngine engine;

	static {
		// disable stupid warnings
		System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");

		// add access
		manager = new ScriptEngineManager();
		engine = createEngine();
	}

	public static CompiledScript compile(final String script) throws ScriptException {
		return ((Compilable) engine).compile(script);
	}

	public static <T> Supplier<T> callable(final String script) {
		final Logger logger = logger();
		try {
			final CompiledScript compiled = compile(script);
			return (() -> {
				final ScriptEngine engine = createEngine();
				try {
					@SuppressWarnings("unchecked")
					T result = (T) compiled.eval(engine.getContext());
					return result;
				} catch (ScriptException e) {
					logger.error("Failed to invoke script", e);
					return null;
				}
			});
		} catch (ScriptException e) {
			logger.error("Failed to load script: %s".formatted(script), e);
			return () -> null;
		}
	}

	public static <T> Supplier<T> callable(final String script, final Map<String, Object> bindings) {
		final Logger logger = logger();
		try {
			CompiledScript compiled = compile(script);
			return (() -> {
				final ScriptEngine engine = createEngine();
				Bindings bindingContext = engine.getBindings(ScriptContext.ENGINE_SCOPE);
				if (null != bindings) {
					bindingContext.putAll(bindings);
				}
				try {
					@SuppressWarnings("unchecked")
					T result = (T) compiled.eval(bindingContext);
					return result;
				} catch (ScriptException e) {
					logger.error("Failed to invoke script", e);
					return null;
				}
			});
		} catch (ScriptException e) {
			logger.error("Failed to load script: %s".formatted(script), e);
			return () -> null;
		}
	}

	private static ScriptEngine createEngine() {
		ScriptEngine engine = manager.getEngineByName(JS_ENGINE);
		engine.getBindings(ScriptContext.ENGINE_SCOPE).put("polyglot.js.allowHostAccess", true);
		return engine;
	}

	private static Logger logger() {
		return LogService.getLogger(ScriptRunner.class);
	}

}
