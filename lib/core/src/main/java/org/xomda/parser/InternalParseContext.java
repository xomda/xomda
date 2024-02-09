package org.xomda.parser;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.xomda.core.config.Configuration;
import org.xomda.core.extension.XOmdaExtension;
import org.xomda.core.util.DeferredActions;
import org.xomda.parser.csv.CsvObject;

public class InternalParseContext implements ParseContext {

	private final Configuration config;
	private final List<? extends XOmdaExtension> extensions;
	private final List<CsvObject> cache = new ArrayList<>();
	private final DeferredActions deferredActions = new DeferredActions();

	public InternalParseContext(final Configuration config) {
		this.config = config;
		extensions = Arrays.stream(config.getExtensions()).map(Class.class::cast).map(c -> {
			@SuppressWarnings("unchecked")
			final Class<? super XOmdaExtension> extensionClass = c;
			return createExtension(extensionClass);
		}).toList();
	}

	static XOmdaExtension createExtension(final Class<? super XOmdaExtension> clazz) {
		try {
			return (XOmdaExtension) clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	public Configuration getConfig() {
		return config;
	}

	public List<? extends XOmdaExtension> getExtensions() {
		return extensions;
	}

	/**
	 * A List of CSV Objects that have been parsed
	 */
	public List<CsvObject> getCache() {
		return cache;
	}

	/**
	 * The complete list of parsed objects, dynamically fetched from the cache
	 */
	public <T> List<T> getObjects() {
		@SuppressWarnings("unchecked")
		final List<T> objects = (List<T>) getCache().stream()
				.map(CsvObject::getProxy)
				.toList();
		return objects;
	}

	public void add(final CsvObject object) {
		cache.add(object);
	}

	public void runDeferred(final Runnable action) {
		deferredActions.add(action);
	}

	public void runDeferred() {
		deferredActions.run();
		deferredActions.clear();
	}

}
