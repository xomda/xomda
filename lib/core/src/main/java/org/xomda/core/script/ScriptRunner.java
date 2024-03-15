package org.xomda.core.script;

import java.util.function.Supplier;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptRunner {

	public static Invocable run(String script) throws ScriptException {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
		engine.eval(script);
		return (Invocable) engine;
	}

	public static <T> Supplier<T> callable(String script) {
		try {
			Invocable invocable = run("function fn(){\n" + script + "\n;}");
			return (() -> {
				try {
					@SuppressWarnings("unchecked")
					T result = (T) invocable.invokeFunction("fn");
					return result;
				} catch (ScriptException | NoSuchMethodException e) {
					return null;
				}
			});
		} catch (ScriptException e) {
			return () -> null;
		}
	}

}
