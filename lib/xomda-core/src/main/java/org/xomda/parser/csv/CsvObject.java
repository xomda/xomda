package org.xomda.parser.csv;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A CSV Object is the result of a single line of CSV that has been parsed into
 * an object. It's an instance of one of the objects defined in the
 * {@link CsvSchema}.
 */
public class CsvObject {

	final Class<?>[] classes;

	private final CsvObjectState state = new CsvObjectState();

	private Object proxy;

	public CsvObject(final Class<?>[] classes) {
		if (null == classes || classes.length < 1) {
			throw new IllegalArgumentException("Specify at least one class");
		}
		this.classes = classes;
	}

	public CsvObject(final Class<?> clazz) {
		this(new Class<?>[] { clazz });
	}

	public CsvObjectState getState() {
		return state;
	}

	public <T> T getValue(final String name) {
		@SuppressWarnings("unchecked")
		final T t = (T) getState().get(name);
		return t;
	}

	public <T> T setValue(final String name, final T value) {
		if (null == value) {
			return null;
		}
		@SuppressWarnings("unchecked")
		final T t = (T) getState().put(name, value);
		return t;
	}

	public <T> T computeIfAbsent(final String name, final Supplier<T> supplier) {
		@SuppressWarnings("unchecked")
		final T result = (T) getState().computeIfAbsent(name, k -> supplier.get());
		return result;
	}

	public Object getProxy() {
		return Optional.ofNullable(proxy).orElseGet(this::createProxy);
	}

	private synchronized Object createProxy() {
		if (null != proxy) {
			return proxy;
		}
		return proxy = CsvProxyObject.create(this);
	}

	public Class<?>[] getClasses() {
		return classes;
	}

	public boolean isInstance(final Class<?> clazz) {
		return null != classes && Arrays.stream(classes).anyMatch(Predicate.isEqual(clazz));
	}

	public boolean isEnum() {
		return null != classes && Arrays.stream(classes).anyMatch(c -> c.isAssignableFrom(Enum.class));
	}

}
