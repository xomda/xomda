package org.xomda.lib.java.renderer;

import java.io.IOException;
import java.util.ArrayList;

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

			Method method = new MethodBean();
			method.setIdentifier("test");
			CreationUtils.addBodyText(method, "return;");

			clz.getMethodList().add(method);
		}

	}

	@Test
	public void test() throws IOException {
		JavaRenderer renderer = new JavaRenderer();
		CompilationUnit unit = new TestCompilationUnit();

		StringBuilder sb = new StringBuilder();
		renderer.render(unit, sb);

		String s = sb.toString();

		System.out.println(s);
	}

}
