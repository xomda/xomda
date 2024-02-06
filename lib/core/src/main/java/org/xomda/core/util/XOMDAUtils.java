package org.xomda.core.util;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.xomda.model.Dependency;
import org.xomda.model.Entity;
import org.xomda.model.Package;

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

}
