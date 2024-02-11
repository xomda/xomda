package org.xomda.plugin.gradle.util;

import org.gradle.api.provider.SetProperty;
import org.xomda.plugin.gradle.XOmdaGradlePluginExtension;
import org.xomda.shared.util.ReflectionUtils;

public class XOmdaGradlePluginUndecoratedExtension implements XOmdaGradlePluginExtension {

	private final Object object;

	public XOmdaGradlePluginUndecoratedExtension(Object object) {
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
