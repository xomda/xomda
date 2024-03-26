package org.xomda.core.module.ast;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.xomda.core.XOMDA;
import org.xomda.core.config.Configuration;
import org.xomda.core.module.XOMDACore;
import org.xomda.lib.java.ast.CompilationUnit;
import org.xomda.lib.java.renderer.JavaRenderer;
import org.xomda.model.Entity;

public class EntityCompilationUnitTest {

	static Entity entity;

	@BeforeAll
	static void before() throws IOException {
		Configuration config = Configuration.builder()
				.withClassPath("org.xomda.model")
				.withExtensions(XOMDACore.class)
				.build();
		List<?> lst = XOMDA.parse("../xomda-model/src/xomda/config/Model.csv", config);
		assertNotNull(lst);
		assertFalse(lst.isEmpty());

		entity = lst.stream()
				.filter(Entity.class::isInstance)
				.map(Entity.class::cast)
				.filter(e -> Objects.equals("Entity", e.getName()))
				.findFirst()
				.orElse(null);

		assertNotNull(entity);
	}

	@Test
	public void test() throws IOException {
		JavaFactory factory = new JavaFactory();
		CompilationUnit unit = factory.create(entity);

		StringBuilder sb = new StringBuilder();

		JavaRenderer r = new JavaRenderer(sb);
		r.render(unit, sb);

		assertFalse(sb.isEmpty());
		System.out.println(sb);
	}

}
