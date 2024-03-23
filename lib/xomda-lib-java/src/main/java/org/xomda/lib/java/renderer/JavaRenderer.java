package org.xomda.lib.java.renderer;

import static org.xomda.shared.exception.SneakyThrow.sneakyConsumer;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.StringJoiner;

import org.xomda.lib.java.ast.Block;
import org.xomda.lib.java.ast.Class;
import org.xomda.lib.java.ast.CompilationUnit;
import org.xomda.lib.java.ast.Constructor;
import org.xomda.lib.java.ast.Field;
import org.xomda.lib.java.ast.Import;
import org.xomda.lib.java.ast.Method;
import org.xomda.lib.java.ast.Type;
import org.xomda.lib.java.ast.Variable;
import org.xomda.shared.exception.SneakyThrow;

public class JavaRenderer {

	private static final char END_OF_STATEMENT = ';';
	private static final char NEW_LINE = '\n';
	private static final char TAB = '\t';
	private int tabs = 0;

	public <T> void render(T obj, Appendable app) throws IOException {
		if (obj instanceof CompilationUnit unit) {
			render(unit, app);

		} else {
			app.append(obj.toString());
		}
	}

	public void nextLine(Appendable app) throws IOException {
		app.append(NEW_LINE);
		for (int i = 0; i < tabs; i++) {
			app.append(TAB);
		}
	}

	public void render(CompilationUnit unit, Appendable app) throws IOException {
		app.append("package ").append(unit.getPackage().getIdentifier()).append(END_OF_STATEMENT);
		nextLine(app);

		unit.getImportList().forEach(SneakyThrow.sneakyConsumer(obj -> render(obj, app)));
		nextLine(app);

		unit.getClassList().forEach(SneakyThrow.sneakyConsumer(obj -> render(obj, app)));
		nextLine(app);
	}

	public void render(Import imp, Appendable app) throws IOException {
		app.append("import ");
		render(imp.getModifier(), app);
		app.append(' ');
		app.append(imp.getIdentifier()).append(END_OF_STATEMENT);
	}

	public void render(org.xomda.lib.java.ast.Modifier mod, Appendable app) throws IOException {
		StringJoiner sj = new StringJoiner(" ");
		if (Modifier.isPrivate(mod.getIdentifier().intValue())) {
			sj.add("private");
		}
		if (Modifier.isPublic(mod.getIdentifier().intValue())) {
			sj.add("public");
		}
		if (Modifier.isProtected(mod.getIdentifier().intValue())) {
			sj.add("protected");
		}
		if (Modifier.isStatic(mod.getIdentifier().intValue())) {
			sj.add("static");
		}
		if (Modifier.isFinal(mod.getIdentifier().intValue())) {
			sj.add("final");
		}
		if (Modifier.isAbstract(mod.getIdentifier().intValue())) {
			sj.add("abstract");
		}
		if (Modifier.isInterface(mod.getIdentifier().intValue())) {
			sj.add("interface");
		}
		if (Modifier.isNative(mod.getIdentifier().intValue())) {
			sj.add("native");
		}
		if (Modifier.isStrict(mod.getIdentifier().intValue())) {
			sj.add("strict");
		}
		if (Modifier.isSynchronized(mod.getIdentifier().intValue())) {
			sj.add("synchronized");
		}
		if (Modifier.isTransient(mod.getIdentifier().intValue())) {
			sj.add("transient");
		}
		if (Modifier.isVolatile(mod.getIdentifier().intValue())) {
			sj.add("volatile");
		}
		app.append(sj.toString());
	}

	public void render(Type type, Appendable app) throws IOException {
		app.append(type.getIdentifier());
		StringJoiner sj = new StringJoiner(", ", "<", ">");
		type.getTypeList().forEach(sneakyConsumer(t -> {
			StringBuilder sb = new StringBuilder();
			render(t, app);
			sj.add(sb);
		}));
		app.append(sj.toString());
	}

	public void render(Variable variable, Appendable app) throws IOException {
		if (null == variable) {
			return;
		}
		app.append(' ').append('=').append(' ');
		app.append(variable.getExpression());
	}

	public void render(Field field, Appendable app) throws IOException {
		field.getModifierList().forEach(sneakyConsumer(m -> render(m, app)));
		app.append(' ');

		app.append(field.getIdentifier());
		render(field.getVariable(), app);
		app.append(END_OF_STATEMENT);
	}

	public void render(Class clazz, Appendable app) throws IOException {
		clazz.getModifierList().forEach(sneakyConsumer(m -> render(m, app)));
		app.append(' ');

		app.append(clazz.getModifierList().stream().anyMatch(m -> Modifier.isInterface(m.getIdentifier().intValue()))
				? "interface"
				: "class"
		);
		app.append(' ');

		app.append(clazz.getIdentifier());
		app.append(' ');

		StringJoiner sj1 = new StringJoiner(", ", "extends ", " ");
		clazz.getExtendsList().forEach(sneakyConsumer(obj -> {
			StringBuilder sb = new StringBuilder();
			render(obj, app);
			sj1.add(sb);
		}));
		app.append(sj1.toString());

		StringJoiner sj2 = new StringJoiner(", ", "implements ", " ");
		clazz.getImplementsList().forEach(sneakyConsumer(obj -> {
			StringBuilder sb = new StringBuilder();
			render(obj, app);
			sj2.add(sb);
		}));
		app.append(sj2.toString());

		app.append('{');

		tabs++;
		nextLine(app);

		if (!clazz.getClassList().isEmpty()) {
			clazz.getClassList().forEach(sneakyConsumer(c -> {
				render(c, app);
				nextLine(app);
			}));
			nextLine(app);
		}

		if (!clazz.getFieldList().isEmpty()) {
			clazz.getFieldList().forEach(sneakyConsumer(field -> {
				render(field, app);
				nextLine(app);
			}));
			nextLine(app);
		}

		if (!clazz.getConstructorList().isEmpty()) {
			clazz.getConstructorList().forEach(sneakyConsumer(field -> {
				render(field, app);
				nextLine(app);
			}));
			nextLine(app);
		}

		if (!clazz.getMethodList().isEmpty()) {
			clazz.getMethodList().forEach(sneakyConsumer(field -> {
				render(field, app);
				nextLine(app);
			}));
			nextLine(app);
		}

		tabs--;
		app.append('}');
		nextLine(app);
	}

	public void render(Constructor constructor, Appendable app) throws IOException {
		constructor.getModifierList().forEach(sneakyConsumer(m -> render(m, app)));
		app.append(' ');

		app.append(constructor.getParentClass().getIdentifier());
		app.append('(');
		constructor.getParameterList().forEach(sneakyConsumer(m -> render(m, app)));
		app.append(')');

		app.append(' ');
		app.append('{');
		tabs++;
		nextLine(app);

		render(constructor.getBlock(), app);

		tabs--;
		app.append('}');
	}

	public void render(Method method, Appendable app) throws IOException {
		method.getModifierList().forEach(sneakyConsumer(m -> render(m, app)));
		app.append(' ');

		if (!method.getGenericList().isEmpty()) {
			app.append('<');
			StringJoiner sj = new StringJoiner(", ");
			method.getGenericList().forEach(sj::add);
			app.append(sj.toString());
			app.append('>');
			app.append(' ');
		}

		app.append(method.getReturntype().getIdentifier());
		app.append(' ');

		app.append('(');
		method.getParameterList().forEach(sneakyConsumer(m -> render(m, app)));
		app.append(')');

		if (null != method.getBlock()) {

			app.append(' ');
			app.append('{');
			tabs++;
			nextLine(app);

			render(method.getBlock(), app);

			tabs--;
			app.append('}');
		} else {
			app.append(END_OF_STATEMENT);
		}
	}

	public void render(Block block, Appendable app) throws IOException {
		block.getTextList().forEach(sneakyConsumer(t -> {
			app.append(t);
			nextLine(app);
		}));
	}

}
