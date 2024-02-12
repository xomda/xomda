package org.xomda.parser;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.xomda.core.config.Configuration;
import org.xomda.core.extension.XOMDAExtension;
import org.xomda.core.util.DeferredActions;
import org.xomda.parser.csv.CsvObject;

public class InternalParseContext implements ParseContext {

	private final Configuration config;
	private final List<CsvObject> cache = new ArrayList<>();
	private final DeferredActions deferredActions = new DeferredActions();

	public InternalParseContext(final Configuration config) {
		this.config = config;
	}

	static XOMDAExtension createExtension(final Class<? super XOMDAExtension> clazz) {
		try {
			return (XOMDAExtension) clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	public Configuration getConfig() {
		return config;
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
