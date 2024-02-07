package org.xomda.core.template;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.xomda.core.java.JavaUtils;
import org.xomda.model.Attribute;
import org.xomda.model.AttributeType;
import org.xomda.model.Entity;
import org.xomda.shared.util.StringUtils;

public class TemplateUtils {

	public static String getJavaPackage(org.xomda.model.Package pkg) {
		if (null == pkg)
			return "";
		String prefix = getJavaPackage(pkg.getPackage()).trim();
		return prefix + (prefix.isBlank() ? "" : ".") + pkg.getPackageName();
	}

	public static String getJavaInterfaceName(org.xomda.model.Entity entity) {
		final String pkg = getJavaPackage(entity.getPackage());
		final String interfaceName = StringUtils.toPascalCase(entity.getName());
		return pkg + "." + interfaceName;
	}

	public static String getJavaEnumName(org.xomda.model.Enum enm) {
		final String pkg = getJavaPackage(enm.getPackage());
		final String interfaceName = StringUtils.toPascalCase(enm.getName());
		return pkg + "." + interfaceName;
	}

	public static Path getPath(org.xomda.model.Package pkg) {
		String pkgName = getJavaPackage(pkg);
		String[] pkgs = pkgName.trim().split("\\.");
		Path parentPath = null;

		for (String p : pkgs)
			parentPath = null == parentPath ? Paths.get(p) : parentPath.resolve(p);

		return parentPath;
	}

	public static Path getEnumPath(org.xomda.model.Enum enm) {
		return getPath(enm.getPackage()).resolve(StringUtils.toPascalCase(enm.getName()) + ".java");
	}

	public static Path getInterfacePath(Entity entity) {
		return getPath(entity.getPackage()).resolve(StringUtils.toPascalCase(entity.getName()) + ".java");
	}

	public static String getJavaType(Entity entity) {
		if (entity == null)
			return "java.lang.Object";
		return getJavaPackage(entity.getPackage()) + "." + StringUtils.toPascalCase(entity.getName());
	}

	public static String getJavaType(org.xomda.model.Enum enm) {
		if (enm == null)
			return "java.lang.Object";
		return getJavaPackage(enm.getPackage()) + "." + StringUtils.toPascalCase(enm.getName());
	}

	public static String getType(Entity entity) {
		return getJavaPackage(entity.getPackage()) + "." + StringUtils.toPascalCase(entity.getName());
	}

	public static String getJavaType(Attribute attribute) {
		AttributeType type = attribute.getType();
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

	public static String getJavaIdentifier(String idr) {
		return JavaUtils.toIdentifier(idr);
	}

}
