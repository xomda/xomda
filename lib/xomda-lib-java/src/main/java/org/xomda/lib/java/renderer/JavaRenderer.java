package org.xomda.lib.java.renderer;

import static org.xomda.shared.exception.SneakyThrow.sneakyConsumer;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Supplier;

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
		each(unit::getImportList, obj -> render(obj, app), () -> nextLine(app));
		each(unit::getClassList, obj -> render(obj, app), () -> nextLine(app));
	}

	public void render(Import imp, Appendable app) throws IOException {
		app.append("import ");
		render(imp.getModifier(), app);
		app.append(' ');
		app.append(imp.getIdentifier()).append(END_OF_STATEMENT);
	}

	public void render(org.xomda.lib.java.ast.Modifier mod, Appendable app) throws IOException {
		if (null == mod) {
			return;
		}
		app.append(Modifier.toString(mod.getIdentifier().intValue()));
	}

	public void render(Type type, Appendable app) throws IOException {
		app.append(type.getIdentifier());
		StringJoiner sj = new StringJoiner(", ", "<", ">");
		each(type::getTypeList, t -> {
			StringBuilder sb = new StringBuilder();
			render(t, app);
			sj.add(sb);
		});
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
		each(field::getModifierList, obj -> render(obj, app), () -> app.append(' '));

		app.append(field.getIdentifier());
		render(field.getVariable(), app);
		app.append(END_OF_STATEMENT);
	}

	public void render(Class clazz, Appendable app) throws IOException {
		each(clazz::getModifierList, obj -> render(obj, app), () -> app.append(' '));
		app.append(clazz.getModifierList() != null && clazz.getModifierList().stream().anyMatch(m -> Modifier.isInterface(m.getIdentifier().intValue()))
				? " " // written by the modifier
				: "class "
		);
		app.append(clazz.getIdentifier());
		app.append(' ');

		StringJoiner sj1 = new StringJoiner(", ", "extends ", " ");
		sj1.setEmptyValue("");
		each(clazz::getExtendsList, obj -> {
			StringBuilder sb = new StringBuilder();
			render(obj, app);
			sj1.add(sb);
		});
		app.append(sj1.toString());

		StringJoiner sj2 = new StringJoiner(", ", "implements ", " ");
		sj2.setEmptyValue("");
		each(clazz::getImplementsList, obj -> {
			StringBuilder sb = new StringBuilder();
			render(obj, app);
			sj1.add(sb);
		});
		app.append(sj2.toString());

		app.append('{');

		tabs++;

		each(clazz::getClassList, c -> {
			nextLine(app);
			render(c, app);
		}, () -> nextLine(app));

		each(clazz::getFieldList, c -> {
			nextLine(app);
			render(c, app);
		}, () -> nextLine(app));

		each(clazz::getConstructorList, c -> {
			nextLine(app);
			render(c, app);
		}, () -> nextLine(app));

		each(clazz::getMethodList, c -> {
			nextLine(app);
			render(c, app);
		}, () -> nextLine(app));

		tabs--;
		nextLine(app);
		app.append('}');
	}

	public void render(Constructor constructor, Appendable app) throws IOException {
		each(constructor::getModifierList, m -> render(m, app), () -> app.append(' '));

		app.append(constructor.getParentClass().getIdentifier());
		app.append('(');
		each(constructor::getParameterList, obj -> render(obj, app));
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

		each(method::getModifierList, m -> render(m, app), () -> app.append(' '));

		if (method.getGenericList() != null && !method.getGenericList().isEmpty()) {
			app.append('<');
			StringJoiner sj = new StringJoiner(", ");
			method.getGenericList().forEach(sj::add);
			app.append(sj.toString());
			app.append('>');
			app.append(' ');
		}

		app.append(Optional.ofNullable(method.getReturntype())
						.map(Type::getIdentifier)
						.orElse("void")
				)
				.append(' ');

		app.append(method.getIdentifier());

		app.append('(');
		each(method::getParameterList, obj -> render(obj, app));
		app.append(')');

		StringJoiner sj1 = new StringJoiner(", ", " throws ", " ");
		sj1.setEmptyValue("");
		each(method::getThrowsList, obj -> {
			StringBuilder sb = new StringBuilder();
			render(obj, app);
			sj1.add(sb);
		});
		app.append(sj1.toString());

		if (null == method.getParentClass() || !RenderUtils.isInterface(method.getParentClass().getModifierList())) {
			app.append(' ');
			app.append('{');

			if (null != method.getBlock()) {
				tabs++;

				render(method.getBlock(), app);

				tabs--;

			}

			nextLine(app);
			app.append('}');
		} else {
			app.append(END_OF_STATEMENT);
		}
	}

	public void render(Block block, Appendable app) throws IOException {
		block.getTextList().forEach(sneakyConsumer(t -> {
			nextLine(app);
			app.append(t);
		}));
	}

	public <T, E extends Throwable> void each(Supplier<Collection<T>> supplier, SneakyThrow.ThrowingConsumer<T, E> consumer) {
		each(supplier, consumer, null);
	}

	public <T, E extends Throwable> void each(Supplier<Collection<T>> supplier, SneakyThrow.ThrowingConsumer<T, E> consumer, SneakyThrow.ThrowingRunnable<E> ifPresent) {
		Optional.ofNullable(supplier.get())
				.ifPresent((Collection<T> col) -> {
					if (null != ifPresent && !col.isEmpty()) {
						ifPresent.run();
					}
					col.forEach(consumer);
				});
	}

}
