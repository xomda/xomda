package org.xomda.core.script;

import static org.xomda.shared.util.ReflectionUtils.unchecked;

import java.util.Map;
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

	public static <T> ScriptInvoker<T> parse(final String script) {
		final Logger logger = logger();
		try {
			CompiledScript compiled = compile(script);
			return ((final Map<String, Object> bindings) -> evaluate(compiled, bindings));
		} catch (ScriptException e) {
			logger.error("Failed to load script: %s".formatted(script), e);
			return null;
		}
	}

	private static CompiledScript compile(final String script) throws ScriptException {
		return ((Compilable) engine).compile(script);
	}

	private static <T> T evaluate(CompiledScript compiled, final Map<String, Object> bindings) {
		try {
			final ScriptEngine engine = createEngine();
			if (null == bindings) {
				return unchecked(compiled.eval(engine.getContext()));
			} else {
				Bindings bindingContext = engine.getBindings(ScriptContext.ENGINE_SCOPE);
				bindingContext.putAll(bindings);
				return unchecked(compiled.eval(bindingContext));
			}
		} catch (ScriptException e) {
			logger().error("Failed to invoke script", e);
			return null;
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
