package org.xomda.plugin.gradle;

import org.gradle.api.provider.SetProperty;

public interface XOmdaGradlePluginExtension {

	/**
	 * Specifies the classpath where to look for interfaces and enums
	 */
	SetProperty<String> getClasspath();

	/**
	 * Specifies the model CSV's to load, instead of just automagically picking them
	 * up
	 */
	SetProperty<String> getModels();

	/**
	 * Specifies the CSV extensions
	 */
	SetProperty<Object> getPlugins();

}
