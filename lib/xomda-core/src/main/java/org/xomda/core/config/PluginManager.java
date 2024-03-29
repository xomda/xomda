package org.xomda.core.config;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.xomda.core.extension.Loggable;
import org.xomda.core.extension.XOMDAExtension;
import org.xomda.core.extension.XOMDAModule;

/**
 * Manages the plugins, from Class to instance
 */
public class PluginManager implements Iterable<XOMDAExtension>, Loggable {

	// LinkedHashMap because it preserves insertion order
	private final Map<Class<? extends XOMDAExtension>, XOMDAExtension> extensions = new LinkedHashMap<>();

	PluginManager(Class<? extends XOMDAExtension>[] extensions) {
		Stream.of(extensions).forEach(this::add);
	}

	public boolean add(Class<? extends XOMDAExtension> clazz) {
		try {
			return add(clazz.getDeclaredConstructor().newInstance());
		} catch (ReflectiveOperationException e) {
			getLogger().error("Failed to load plugin: {}", clazz, e);
			return false;
		}
	}

	public boolean add(XOMDAExtension extension) {
		Class<? extends XOMDAExtension> clazz = extension.getClass();
		if (extensions.containsKey(clazz)) {
			getLogger().warn("Plugin already loaded: {}", clazz.getName());
			return false;
		}

		extensions.put(clazz, extension);

		if (extension instanceof XOMDAModule module) {
			return StreamSupport
					.stream(module.spliterator(), false)
					.allMatch(this::add);
		}
		return true;
	}

	public Stream<XOMDAExtension> findByType(Class<? extends XOMDAExtension> clazz) {
		return extensions.keySet().stream()
				.filter(clazz::isAssignableFrom)
				.map(extensions::get);
	}

	public Stream<XOMDAExtension> stream() {
		return extensions.values().stream();
	}

	@Override
	public Iterator<XOMDAExtension> iterator() {
		return extensions.values().iterator();
	}
}
