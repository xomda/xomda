package org.xomda.core.config;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.logging.log4j.Level;
import org.slf4j.Logger;
import org.xomda.core.extension.XOmdaExtension;
import org.xomda.core.template.Template;
import org.xomda.model.Entity;
import org.xomda.shared.logging.LogService;

/**
 * The Configuration of XOMDA is done here.
 * <p>
 * It should hold all settings which are externally configurable. Addons, like
 * the CLI or the Gradle plugin, will each parse their settings into a
 * Configuration.
 */
public interface Configuration {

	String[] DEFAULT_CLASSPATH = {Entity.class.getPackageName()};
	Level DEFAULT_LOG_LEVEL = Level.INFO;

	String[] getClasspath();

	void setClasspath(String[] classpath);

	Level getLogLevel();

	void setLogLevel(Level logLevel);

	default void setLogLevel(String logLevel) {
		setLogLevel(Level.valueOf(logLevel));
	}

	default void setLogLevel(Object logLevel) {
		if (logLevel instanceof Enum<?> en)
			en.name();
		else if (logLevel instanceof CharSequence cs)
			setLogLevel(cs.toString());
	}

	Class<? extends XOmdaExtension>[] getExtensions();

	void setExtensions(Class<? extends XOmdaExtension>[] extensions);

	default Stream<Class<? extends XOmdaExtension>> extensions() {
		return toStream(getExtensions());
	}

	String[] getModels();

	void setModels(String[] models);

	default Stream<String> models() {
		return toStream(getModels());
	}

	String getOutDir();

	void setOutDir(String outDir);

	// TODO: MOVE!
	@SuppressWarnings("unchecked")
	default Stream<Template<Object>> getTemplates() {
		return extensions().filter(Template.class::isAssignableFrom).map(c -> {
			try {
				return c.getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				return null;
			}
		}).filter(Objects::nonNull).map(c -> (Template<Object>) c);
	}

	default Logger getLogger(Class<?> clazz) {
		return LogService.getLogger(clazz);
	}

	static ConfigurationBuilder builder() {
		return new ConfigurationBuilder();
	}

	private static <T> Stream<T> toStream(T[] array) {
		return null == array ? Stream.empty() : Arrays.stream(array);
	}
}
