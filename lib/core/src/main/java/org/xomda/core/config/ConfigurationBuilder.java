package org.xomda.core.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.Level;
import org.xomda.core.extension.Loggable;
import org.xomda.core.extension.XOmdaExtension;
import org.xomda.core.module.XOmdaReverseEntity;
import org.xomda.core.module.XOmdaTypeRefs;
import org.xomda.core.module.template.XOmdaCodeTemplate;
import org.xomda.shared.util.ReflectionUtils;

/**
 * Builder for {@link Configuration}. It has helper methods for handling
 * collections, because the {@link Configuration} works with bare arrays.
 */
public final class ConfigurationBuilder implements Loggable {

	private static final Collection<Object> DEFAULT_OMDA_EXTENSIONS = Set.of(
			// TODO -> OmdaModule
			XOmdaReverseEntity.class, XOmdaTypeRefs.class, XOmdaCodeTemplate.class);

	private Collection<Class<? extends XOmdaExtension>> extensions = ConcurrentHashMap.newKeySet();
	private Set<String> classpath = ConcurrentHashMap.newKeySet();
	private Set<String> models = ConcurrentHashMap.newKeySet();
	private Set<String> dependentModels = ConcurrentHashMap.newKeySet();
	private String outDir = System.getProperty("user.dir");
	private Level logLevel;

	public ConfigurationBuilder create() {
		return new ConfigurationBuilder();
	}

	public Configuration build() {
		final Impl impl = new Impl();
		@SuppressWarnings("unchecked")
		final Class<? extends XOmdaExtension>[] extensions = this.extensions.toArray(Class[]::new);
		impl.extensions = extensions;
		impl.classpath = classpath.toArray(String[]::new);
		impl.models = models.toArray(String[]::new);
		impl.dependentModels = dependentModels.toArray(String[]::new);
		impl.outDir = outDir;
		impl.logLevel = logLevel;
		return impl;
	}

	public ConfigurationBuilder withDefaultOmdaExtensions() {
		addClassPath(Configuration.DEFAULT_CLASSPATH);
		return addExtensions(DEFAULT_OMDA_EXTENSIONS);
	}

	public ConfigurationBuilder withExtensions(final Object... extensions) {
		this.extensions = ConcurrentHashMap.newKeySet();
		return addExtensions(extensions);
	}

	public ConfigurationBuilder withExtensions(final Collection<Object> extensions) {
		this.extensions = ConcurrentHashMap.newKeySet();
		return addExtensions(extensions);
	}

	public ConfigurationBuilder addExtensions(final Collection<Object> extensions) {
		this.extensions.addAll(extensions.stream().map(this::toExtension).filter(Objects::nonNull).distinct().toList());
		return this;
	}

	public ConfigurationBuilder addExtensions(final Object... extensions) {
		if (null != extensions) {
			addExtensions(Arrays.asList(extensions));
		}
		return this;
	}

	public ConfigurationBuilder withClassPath(final String... classPath) {
		if (null != classPath) {
			classpath = ConcurrentHashMap.newKeySet();
		}
		return addClassPath(classPath);
	}

	public ConfigurationBuilder withClassPath(final Collection<String> classPath) {
		classpath = ConcurrentHashMap.newKeySet();
		return addClassPath(classPath);
	}

	public ConfigurationBuilder addClassPath(final String... classPath) {
		if (null != classPath) {
			classpath.addAll(Arrays.asList(classPath));
		}
		return this;
	}

	public ConfigurationBuilder addClassPath(final Collection<String> classPath) {
		classpath.addAll(classPath);
		return this;
	}

	public ConfigurationBuilder withModels(final String... models) {
		this.models = ConcurrentHashMap.newKeySet();
		return addModels(models);
	}

	public ConfigurationBuilder withModels(final Collection<String> models) {
		this.models = ConcurrentHashMap.newKeySet();
		return addModels(models);
	}

	public ConfigurationBuilder addModels(final String... models) {
		if (null != models) {
			this.models.addAll(Arrays.asList(models));
		}
		return this;
	}

	public ConfigurationBuilder addModels(final Collection<String> models) {
		this.models.addAll(models);
		return this;
	}

	public ConfigurationBuilder withDependentModels(final String... models) {
		dependentModels = ConcurrentHashMap.newKeySet();
		return addDependentModels(models);
	}

	public ConfigurationBuilder withDependentModels(final Collection<String> models) {
		dependentModels = ConcurrentHashMap.newKeySet();
		return addDependentModels(models);
	}

	public ConfigurationBuilder addDependentModels(final String... models) {
		if (null != models) {
			this.dependentModels.addAll(Arrays.asList(models));
		}
		return this;
	}

	public ConfigurationBuilder addDependentModels(final Collection<String> models) {
		this.dependentModels.addAll(models);
		return this;
	}

	public ConfigurationBuilder withOutDir(final String outDir) {
		this.outDir = outDir;
		return this;
	}

	public ConfigurationBuilder withLogLevel(final Level logLevel) {
		this.logLevel = logLevel;
		return this;
	}

	public ConfigurationBuilder withLogLevel(final String logLevel) {
		this.logLevel = Level.valueOf(logLevel);
		return this;
	}

	public ConfigurationBuilder withLogLevel(final Object logLevel) {
		if (logLevel instanceof final Enum<?> enm) {
			return withLogLevel(enm.name());
		}
		if (logLevel instanceof final String s) {
			return withLogLevel(s);
		}
		if (logLevel instanceof final Level l) {
			return withLogLevel(l);
		}
		return this;
	}

	Class<? extends XOmdaExtension> toExtension(Object o) {
		// if the argument is a string, we need to try to turn it into a class
		if (o instanceof final String clz) {
			final Optional<Class<Object>> clazz = ReflectionUtils.findClass(clz);
			if (clazz.isPresent()) {
				o = clazz.get();
			} else {
				getLogger().warn("Failed to load {}.", clz);
			}
		}
		// check if we have a class
		if (o instanceof final Class<?> clz) {
			try {
				@SuppressWarnings("unchecked")
				final Class<? extends XOmdaExtension> result = (Class<? extends XOmdaExtension>) clz;
				return result;
			} catch (final Exception e) {
				// noop
			}
			// check if the class is meaningful to the plugin
			if (ReflectionUtils.extendsFrom(clz, XOmdaExtension.class)) {
				@SuppressWarnings("unchecked")
				final Class<? extends XOmdaExtension> result = (Class<? extends XOmdaExtension>) clz;
				return result;
			} else {
				getLogger().warn("{} is not a CSV Extension and cannot be used at this point.", clz);
			}
		}
		return null;
	}

	static private class Impl implements Configuration {

		private String outDir;
		private String[] classpath;
		private Level logLevel;
		private String[] models;
		private String[] dependentModels;

		private Class<? extends XOmdaExtension>[] extensions;

		@Override
		public String[] getClasspath() {
			return classpath;
		}

		@Override
		public void setClasspath(final String[] classpath) {
			this.classpath = classpath;
		}

		@Override
		public Level getLogLevel() {
			return logLevel;
		}

		@Override
		public void setLogLevel(final Level logLevel) {
			if (null != logLevel) {
				this.logLevel = logLevel;
			}
		}

		@Override
		public Class<? extends XOmdaExtension>[] getExtensions() {
			return extensions;
		}

		@Override
		public void setExtensions(final Class<? extends XOmdaExtension>[] extensions) {
			this.extensions = extensions;
		}

		@Override
		public String[] getModels() {
			return models;
		}

		@Override
		public void setModels(final String[] models) {
			this.models = models;
		}

		@Override
		public String[] getDependentModels() {
			return dependentModels;
		}

		@Override
		public void setDependentModels(final String[] models) {
			this.dependentModels = models;
		}

		@Override
		public String getOutDir() {
			return outDir;
		}

		@Override
		public void setOutDir(final String outDir) {
			this.outDir = outDir;
		}

	}

}
