package org.xomda.shared.util;

import static org.xomda.shared.exception.SneakyThrow.sneaky;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.xomda.shared.logging.LogService;

public class ReflectionUtils {

	public static <T> Optional<Class<T>> findClass(final String className) {
		try {
			@SuppressWarnings("unchecked")
			final Class<T> found = (Class<T>) Class.forName(className);
			return Optional.of(found);
		} catch (final ClassNotFoundException e) {
			return Optional.empty();
		}
	}

	public static <T> Optional<Class<T>> findClass(final String className, final boolean initialize, final ClassLoader classLoader) {
		try {
			@SuppressWarnings("unchecked")
			final Class<T> found = (Class<T>) Class.forName(className, initialize, classLoader);
			return Optional.of(found);
		} catch (final ClassNotFoundException e) {
			LogService
					.getLogger(ReflectionUtils.class)
					.debug("Failed to load {}", className);
			return Optional.empty();
		}
	}

	public static <T> Optional<Class<T>> findClass(final String className, final ClassLoader classLoader) {
		return findClass(className, true, classLoader);
	}

	public static Function<Object, Object> getGetterFunction(final Class<?> clazz, final String name) {
		return getGetter(clazz, name).<Function<Object, Object>> map(method -> (final Object obj) -> {
			try {
				return method.invoke(obj);
			} catch (final IllegalAccessException | InvocationTargetException e) {
				// throw new RuntimeException(e);
				return null;
			}
		}).orElseGet(() -> (final Object obj) -> {
			return null;
		});

	}

	@SuppressWarnings({ "rawtypes" })
	public static Predicate<Class> extendsFrom(final Class generic) {
		return (final Class clz) -> extendsFrom(clz, generic);
	}

	@SuppressWarnings({ "rawtypes" })
	public static boolean extendsFrom(final Class clz, final Class generic) {
		return clz == generic
				|| Stream.concat(Stream.of(clz.getGenericSuperclass()), Stream.of(clz.getGenericInterfaces()))
				.filter(Objects::nonNull).filter(Class.class::isInstance).map(Class.class::cast)
				.anyMatch(extendsFrom(generic));
	}

	public static Optional<Method> getGetter(final Class<?> clazz, final String name) {
		if (null == name || name.isBlank()) {
			throw new IllegalArgumentException("The name should not be blank or null");
		}
		final String trimmed = name.trim();
		final String getterName = "get" + Character.toUpperCase(trimmed.charAt(0)) + trimmed.substring(1);
		return Arrays.stream(clazz.getDeclaredMethods()).filter(isGetter(getterName)).findFirst();
	}

	public static <T> Supplier<T> getGetterSupplier(final Object obj, final String name) {
		return getGetter(obj.getClass(), name)
				.map((Method m) -> {
							@SuppressWarnings("unchecked")
							Supplier<T> supplier = (Supplier<T>) sneaky(() -> m.invoke(obj));
							return supplier;
						}
				)
				.orElseGet(() -> () -> null);
	}

	private static Predicate<Method> isGetter(final String methodName) {
		return (final Method method) -> method.getParameterCount() == 0 && method.getName().equals(methodName);
	}

	public static <P> P unchecked(final Object p) {
		@SuppressWarnings("unchecked")
		final P r = (P) p;
		return r;
	}

	/**
	 * Resolves array types. ie, <code>Class&lt;T[]&gt;</code> becomes
	 * <code>Class&lt;T&gt;</code>
	 */
	public static <C> Class<?> getBareType(final Class<?> clazz) {
		return null == clazz ? null : clazz.isArray() ? clazz.getComponentType() : clazz;
	}

}
