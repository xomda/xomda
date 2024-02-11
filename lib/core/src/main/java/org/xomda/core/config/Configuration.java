package org.xomda.core.config;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.logging.log4j.Level;
import org.xomda.core.extension.Loggable;
import org.xomda.core.extension.XOMDAExtension;
import org.xomda.model.Entity;
import org.xomda.shared.util.StreamUtils;
import org.xomda.template.Template;

/**
 * The Configuration of XOMDA is done here.
 * <p>
 * It should hold all settings which are externally configurable. Addons, like
 * the CLI or the Gradle plugin, will each parse their settings into a
 * Configuration.
 */
public interface Configuration extends Loggable {

	String[] DEFAULT_CLASSPATH = { Entity.class.getPackageName() };
	Level DEFAULT_LOG_LEVEL = Level.INFO;

	String[] getClasspath();

	void setClasspath(String[] classpath);

	Level getLogLevel();

	void setLogLevel(Level logLevel);

	default void setLogLevel(final String logLevel) {
		setLogLevel(Level.valueOf(logLevel));
	}

	default void setLogLevel(final Object logLevel) {
		if (logLevel instanceof final Enum<?> en) {
			en.name();
		} else if (logLevel instanceof final CharSequence cs) {
			setLogLevel(cs.toString());
		}
	}

	Class<? extends XOMDAExtension>[] getExtensions();

	void setExtensions(Class<? extends XOMDAExtension>[] extensions);

	default Stream<Class<? extends XOMDAExtension>> extensions() {
		return toStream(getExtensions());
	}

	String[] getModels();

	void setModels(String[] models);

	String[] getDependentModels();

	void setDependentModels(final String[] models);

	String getOutDir();

	void setOutDir(String outDir);

	// TODO: MOVE!
	@SuppressWarnings("unchecked")
	default Stream<Template<Object>> getTemplates() {
		return extensions().filter(Template.class::isAssignableFrom)
				.map(StreamUtils.mapOrNull(c -> c.getDeclaredConstructor().newInstance()))
				.filter(Objects::nonNull)
				.map(c -> (Template<Object>) c);
	}

	static ConfigurationBuilder builder() {
		return new ConfigurationBuilder();
	}

	private static <T> Stream<T> toStream(final T[] array) {
		return null == array ? Stream.empty() : Arrays.stream(array);
	}
}
