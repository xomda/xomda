package org.xomda.template;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TemplateContext {

	private final String workingDir;
	private final List<?> parseResult;
	private final Map<Object, Object> userData = new ConcurrentHashMap<>();

	public TemplateContext(final String workingDir, List<?> parseResult) {
		this.workingDir = workingDir;
		this.parseResult = parseResult;
	}

	public Map<Object, Object> userData() {
		return userData;
	}

	public String cwd() {
		return workingDir;
	}

	public List<?> getParseResults() {
		return parseResult;
	}

}
