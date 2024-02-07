package org.xomda.core.template;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TemplateContext {

	private final String outDir;
	private final Map<Object, Object> userData = new ConcurrentHashMap<>();

	public TemplateContext(final String outDir) {
		this.outDir = outDir;
	}

	public Map<Object, Object> userData() {
		return userData;
	}

	public String outDir() {
		return outDir;
	}

	public static TemplateContext defaultContext() {
		return new TemplateContext(System.getProperty("user.dir"));
	}

}
