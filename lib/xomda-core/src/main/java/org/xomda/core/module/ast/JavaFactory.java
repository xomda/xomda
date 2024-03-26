package org.xomda.core.module.ast;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.xomda.lib.java.ast.Class;
import org.xomda.lib.java.ast.CompilationUnit;
import org.xomda.lib.java.ast.Type;
import org.xomda.lib.java.ast.impl.PackageImpl;
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
			return naming.getType(entity);
		}

	}

	public CompilationUnit create(Entity entity) {
		String name = naming.getClassName(entity);
		CompilationUnit unit = new EntityCompilationUnit(entity) {
			@Override
			public String getIdentifier() {
				return name;
			}
		};
		unit.setPackage(new PackageImpl() {
			@Override
			public String getIdentifier() {
				return naming.getClassPackage(entity);
			}
		});

		Class clz = new EntityClass(entity) {
			@Override
			public String getIdentifier() {
				return name;
			}
		};

		unit.setClassList(List.of(clz));

		return unit;
	}

}
