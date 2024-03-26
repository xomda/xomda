package org.xomda.core.module.ast;

import org.xomda.model.Entity;
import org.xomda.shared.util.StringUtils;
import org.xomda.template.TemplateUtils;

public interface NamingConventions {

	default String getFullyQualifiedInterfaceName(Entity entity) {
		return getInterfacePackage(entity) + "." + getInterfaceName(entity);
	}

	default String getFullyQualifiedClassName(Entity entity) {
		return getClassPackage(entity) + "." + getClassName(entity);
	}

	default String getInterfaceName(Entity entity) {
		return StringUtils.toPascalCase(entity.getName());
	}

	default String getClassName(Entity entity) {
		return StringUtils.toPascalCase(entity.getName());
	}

	default String getInterfacePackage(Entity entity) {
		return TemplateUtils.getJavaPackage(entity.getPackage());
	}

	default String getClassPackage(Entity entity) {
		return TemplateUtils.getJavaPackage(entity.getPackage());
	}

}
