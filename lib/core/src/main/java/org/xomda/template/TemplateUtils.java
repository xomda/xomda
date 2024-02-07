package org.xomda.template;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.xomda.core.java.JavaUtils;
import org.xomda.model.Attribute;
import org.xomda.model.AttributeType;
import org.xomda.model.Entity;
import org.xomda.shared.util.StringUtils;

public class TemplateUtils {

	public static String getJavaPackage(final org.xomda.model.Package pkg) {
		if (null == pkg) {
			return "";
		}
		final String prefix = getJavaPackage(pkg.getPackage()).trim();
		return prefix + (prefix.isBlank() ? "" : ".") + pkg.getPackageName();
	}

	public static String getJavaInterfaceName(final org.xomda.model.Entity entity) {
		final String pkg = getJavaPackage(entity.getPackage());
		final String interfaceName = StringUtils.toPascalCase(entity.getName());
		return pkg + "." + interfaceName;
	}

	public static String getJavaEnumName(final org.xomda.model.Enum enm) {
		final String pkg = getJavaPackage(enm.getPackage());
		final String interfaceName = StringUtils.toPascalCase(enm.getName());
		return pkg + "." + interfaceName;
	}

	public static Path getPath(final org.xomda.model.Package pkg) {
		final String pkgName = getJavaPackage(pkg);
		final String[] pkgs = pkgName.trim().split("\\.");
		Path parentPath = null;

		for (final String p : pkgs) {
			parentPath = null == parentPath ? Paths.get(p) : parentPath.resolve(p);
		}

		return parentPath;
	}

	public static Path getEnumPath(final org.xomda.model.Enum enm) {
		return getPath(enm.getPackage()).resolve(StringUtils.toPascalCase(enm.getName()) + ".java");
	}

	public static Path getInterfacePath(final Entity entity) {
		return getPath(entity.getPackage()).resolve(StringUtils.toPascalCase(entity.getName()) + ".java");
	}

	public static String getJavaType(final Entity entity) {
		if (entity == null) {
			return "java.lang.Object";
		}
		return getJavaPackage(entity.getPackage()) + "." + StringUtils.toPascalCase(entity.getName());
	}

	public static String getJavaType(final org.xomda.model.Enum enm) {
		if (enm == null) {
			return "java.lang.Object";
		}
		return getJavaPackage(enm.getPackage()) + "." + StringUtils.toPascalCase(enm.getName());
	}

	public static String getType(final Entity entity) {
		return getJavaPackage(entity.getPackage()) + "." + StringUtils.toPascalCase(entity.getName());
	}

	public static String getJavaType(final Attribute attribute) {
		final AttributeType type = attribute.getType();
		if (null == type) {
			return "java.lang.Object";
		}
		return switch (type) {
			case String, Text -> "java.lang.String";
			case Boolean -> "java.lang.Boolean";
			case Integer -> "java.lang.Long";
			case Decimal -> "java.lang.Double";
			case Date, Time, Timestamp -> "java.util.Date";

			case Entity -> getType(attribute.getEntityRef());
			case Enum -> getJavaType(attribute.getEnumRef());

			default -> "java.lang.Object";
		};
	}

	public static String getJavaIdentifier(final String idr) {
		return JavaUtils.toIdentifier(idr);
	}

}
