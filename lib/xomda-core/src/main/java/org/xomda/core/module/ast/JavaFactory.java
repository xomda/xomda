package org.xomda.core.module.ast;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.xomda.lib.java.ast.Class;
import org.xomda.lib.java.ast.CompilationUnit;
import org.xomda.lib.java.ast.Type;
import org.xomda.lib.java.ast.impl.TypeImpl;
import org.xomda.model.Entity;

public class JavaFactory {

	Map<Entity, Type> typeMap = new ConcurrentHashMap<>();
	NamingConventions naming = new NamingConventions() {
	};

	class DynamicInterfaceType extends TypeImpl {

		Entity entity = null;

		@Override
		public String getIdentifier() {
			return naming.getFullyQualifiedInterfaceName(entity);
		}

	}

	public CompilationUnit create(Entity entity) {
		CompilationUnit unit = new EntityCompilationUnit(entity);
		Class clz = new EntityClass(entity);

		unit.setClassList(List.of(clz));

		return unit;
	}

}
