package org.xomda.lib.java.renderer;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.xomda.lib.java.ast.Class;
import org.xomda.lib.java.ast.ClassBean;
import org.xomda.lib.java.ast.CompilationUnit;
import org.xomda.lib.java.ast.CompilationUnitBean;
import org.xomda.lib.java.ast.Method;
import org.xomda.lib.java.ast.MethodBean;
import org.xomda.lib.java.ast.Package;
import org.xomda.lib.java.ast.PackageBean;

public class JavaRenderTest {

	class TestCompilationUnit extends CompilationUnitBean {

		TestCompilationUnit() {
			setClassList(new ArrayList<>());
			setImportList(new ArrayList<>());

			Package pkg = new PackageBean();
			pkg.setIdentifier("org.xomda.test");
			setPackage(pkg);

			Class clz = new ClassBean();
			clz.setClassList(new ArrayList<>());
			clz.setExtendsList(new ArrayList<>());
			clz.setImplementsList(new ArrayList<>());
			clz.setModifierList(new ArrayList<>());
			clz.setFieldList(new ArrayList<>());
			clz.setMethodList(new ArrayList<>());
			clz.setConstructorList(new ArrayList<>());

			clz.setIdentifier("TestClass");
			getClassList().add(clz);

			Method m1 = new MethodBean();
			m1.setIdentifier("test");
			CreationUtils.addBodyText(m1, "return;");
			clz.getMethodList().add(m1);

			org.xomda.lib.java.ast.Modifier staticModifier = new org.xomda.lib.java.ast.ModifierBean();
			staticModifier.setIdentifier(Long.valueOf(Modifier.STATIC | Modifier.PUBLIC));

			Method m2 = new MethodBean();
			m2.setIdentifier("main");
			m2.setModifierList(List.of(staticModifier));
			CreationUtils.addBodyText(m2, "return;");
			clz.getMethodList().add(m2);
		}

	}

	@Test
	public void test() throws IOException {
		StringBuilder sb = new StringBuilder();
		JavaRenderer renderer = new JavaRenderer(sb);
		CompilationUnit unit = new TestCompilationUnit();

		renderer.render(unit);

		assertFalse(sb.isEmpty());
		String s = sb.toString();

		System.out.println(s);
	}

}
