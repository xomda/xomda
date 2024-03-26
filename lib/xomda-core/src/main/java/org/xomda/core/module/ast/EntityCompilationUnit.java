package org.xomda.core.module.ast;

import org.xomda.lib.java.ast.impl.CompilationUnitImpl;
import org.xomda.model.Entity;

public class EntityCompilationUnit extends CompilationUnitImpl {

	private final Entity entity;

	public EntityCompilationUnit(Entity entity) {
		this.entity = entity;
	}

}
