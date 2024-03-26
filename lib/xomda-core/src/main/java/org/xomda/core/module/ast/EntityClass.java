package org.xomda.core.module.ast;

import static org.xomda.core.module.ast.AstUtils.createModifiers;
import static org.xomda.core.module.ast.AstUtils.createType;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.xomda.lib.java.ast.Method;
import org.xomda.lib.java.ast.impl.ClassImpl;
import org.xomda.lib.java.ast.impl.MethodImpl;
import org.xomda.lib.java.renderer.CreationUtils;
import org.xomda.model.Attribute;
import org.xomda.model.Entity;
import org.xomda.shared.util.StringUtils;

public class EntityClass extends ClassImpl {

	private final Entity entity;

	public EntityClass(Entity entity) {
		this.entity = entity;
	}

	@Override
	public List<Method> getMethodList() {
		if (null == entity.getAttributeList()) {
			return Collections.emptyList();
		}
		return entity.getAttributeList().stream()
				.flatMap(this::createMethods)
				.toList();
	}

	private Stream<Method> createMethods(Attribute att) {
		String name = StringUtils.toPascalCase(att.getName());

		Method getter = new MethodImpl();
		getter.setModifierList(createModifiers(Modifier.PUBLIC));
		getter.setIdentifier("get%s".formatted(name));
		getter.setReturntype(createType(att));
		CreationUtils.addBodyText(getter, "return null;");

		Method setter = new MethodImpl();
		setter.setModifierList(createModifiers(Modifier.PUBLIC));
		setter.setIdentifier("set%s".formatted(name));

		return Stream.of(getter, setter);
	}

}