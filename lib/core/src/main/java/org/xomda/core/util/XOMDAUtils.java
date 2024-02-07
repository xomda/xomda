package org.xomda.core.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.xomda.core.extension.XOmdaExtension;
import org.xomda.model.Dependency;
import org.xomda.model.Entity;
import org.xomda.model.Package;
import org.xomda.shared.util.Predicates;
import org.xomda.shared.util.ReflectionUtils;
import org.xomda.template.Template;

public class XOMDAUtils {

	public static Stream<Entity> findReverseEntities(final Entity entity) {
		if (null == entity) {
			return Stream.empty();
		}
		Package root = entity.getPackage();
		while (null != root.getPackage()) {
			root = root.getPackage();
		}
		return getAllEntities(entity.getPackage()).filter(e -> stream(e::getAttributeList)
				.anyMatch(a -> Dependency.Composite.equals(a.getDependency()) && entity.equals(a.getEntityRef())));
	}

	private static Stream<Entity> getAllEntities(final Package pkg) {
		return null == pkg
				? Stream.empty()
				: Stream.concat(
						stream(pkg::getPackageList).flatMap(XOMDAUtils::getAllEntities),
						stream(pkg::getEntityList)
				);
	}

	private static <T> Stream<T> stream(final Supplier<Collection<T>> supplier) {
		final Collection<T> col = supplier.get();
		return null == col ? Stream.empty() : col.stream();
	}

	public static boolean isExtensionClass(final Class<?> o) {
		return getGenericInterfaceClasses(o).anyMatch(XOmdaExtension.class::isAssignableFrom);
	}

	public static boolean isTemplateClass(final Class<?> o) {
		return getExtensionTypes(o, Template.class).anyMatch(Predicates.alwaysTrue());
	}

	@SuppressWarnings("rawtypes")
	public static boolean isTemplateClass(final Class<?> o, final Class<?> ext) {
		return getExtensionTypes(o, Template.class)
				.map(Class.class::cast)
				.anyMatch((final Class c) -> hasGeneric(c, ext));
	}

	static boolean hasGeneric(final Class<?> c, final Class<?> generic) {
		return getGenericInterfaces(c)
				.filter(ParameterizedType.class::isInstance)
				.map(ParameterizedType.class::cast)
				.anyMatch(pt -> Stream.of(pt.getActualTypeArguments())
						.filter(Class.class::isInstance)
						.map(Class.class::cast)
						.anyMatch(generic::isAssignableFrom)
				);
	}

	public static <E extends XOmdaExtension> Stream<?> getExtensionTypes(final Class<?> o, final Class<E> ext) {
		return getGenericInterfaceClasses(o)
				.filter(ext::isAssignableFrom);
	}

	static Stream<Type> getGenericInterfaces(final Type t) {
		return ReflectionUtils.unchecked(Stream.concat(
				Stream.concat(
						Stream.of(t),
						Stream.of(t)
								.filter(Class.class::isInstance)
								.map(Class.class::cast)
								.flatMap(c -> Stream.of(c.getGenericInterfaces()))
								.flatMap(XOMDAUtils::getGenericInterfaces)
				),

				t instanceof final Class tc ? getGenericInterfaceClasses((tc).getSuperclass())
						: Stream.empty()
		));
	}

	static Stream<Class<?>> getGenericInterfaceClasses(final Class<?> o) {
		return ReflectionUtils.unchecked(getGenericInterfaces(o)
				.filter(Class.class::isInstance)
				.map(Class.class::cast));
	}

}
