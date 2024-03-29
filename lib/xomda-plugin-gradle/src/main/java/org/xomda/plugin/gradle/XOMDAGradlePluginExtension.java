package org.xomda.plugin.gradle;

import org.gradle.api.provider.SetProperty;

public interface XOMDAGradlePluginExtension {

	/**
	 * Specifies the classpath where to look for interfaces and enums
	 */
	SetProperty<String> getClasspath();

	/**
	 * Specifies the model definitions to load, instead of just automagically picking them
	 * up
	 */
	SetProperty<String> getModels();

	/**
	 * Specifies the XOMDA extensions
	 */
	SetProperty<Object> getPlugins();

}
