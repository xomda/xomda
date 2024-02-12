package org.xomda.core.config;

import org.apache.logging.log4j.Level;
import org.xomda.core.extension.Loggable;
import org.xomda.model.Entity;

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

	PluginManager getPlugins();

	String[] getClasspath();

	Level getLogLevel();

	String[] getModels();

	String[] getDependentModels();

	String getOutDir();

	static ConfigurationBuilder builder() {
		return new ConfigurationBuilder();
	}

}
