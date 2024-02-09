package org.xomda.template;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TemplateContext {

	private final String outDir;
	private final List<?> parseResult;
	private final Map<Object, Object> userData = new ConcurrentHashMap<>();

	public TemplateContext(final String outDir, List<?> parseResult) {
		this.outDir = outDir;
		this.parseResult = parseResult;
	}

	public Map<Object, Object> userData() {
		return userData;
	}

	public String outDir() {
		return outDir;
	}

	public List<?> getParseResults() {
		return parseResult;
	}

}
