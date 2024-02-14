package org.xomda.plugin.gradle.util;

import org.gradle.api.provider.SetProperty;
import org.xomda.plugin.gradle.XOMDAGradlePluginExtension;
import org.xomda.shared.util.ReflectionUtils;

/**
 * This dirty bastard gets XOMDA configurations from dependent projects.
 * The problem is that these configurations seem to be "Decorated", but also have different classloaders.
 * So instead of trying to cast them (which would throw a ClassCastException),
 * we're invoking the getters using reflection. 🤢
 * <p>
 * TODO: This technique does the job, but should be refactored when possible.
 * 💡 The XOMDA gradle plugin uses a custom classloader itself ({@link XOMDATemplateClassLoader}),
 * it may be that this is the cause of the problem.
 */
public class XOMDAGradlePluginUndecoratedExtension implements XOMDAGradlePluginExtension {

	private final Object object;

	public XOMDAGradlePluginUndecoratedExtension(Object object) {
		this.object = object;
	}

	@SuppressWarnings("unchecked")
	public SetProperty<String> getClasspath() {
		return (SetProperty<String>) ReflectionUtils.getGetterSupplier(object, "classpath").get();
	}

	@SuppressWarnings("unchecked")
	public SetProperty<String> getModels() {
		return (SetProperty<String>) ReflectionUtils.getGetterSupplier(object, "models").get();
	}

	@SuppressWarnings("unchecked")
	public SetProperty<Object> getPlugins() {
		return (SetProperty<Object>) ReflectionUtils.getGetterSupplier(object, "plugins").get();
	}

}
