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
import org.xomda.lib.java.ast.Package;
import org.xomda.lib.java.ast.Type;
import org.xomda.lib.java.ast.Variable;
import org.xomda.lib.java.formatter.DefaultJavaFormatter;
import org.xomda.lib.java.formatter.JavaFormatter;
import org.xomda.shared.exception.SneakyThrow;

public class JavaRenderer {

	private static final char END_OF_STATEMENT = ';';

	// only if needed
	private static final char SEPARATOR = ' ';

	private final Appendable appendable;
	private final JavaFormatter formatter;

	public JavaRenderer(Appendable appendable, JavaFormatter formatter) {
		this.appendable = appendable;
		this.formatter = formatter;
	}

	public JavaRenderer(Appendable appendable) {
		this(appendable, new DefaultJavaFormatter(appendable));
	}

	public <T> void render(T obj) throws IOException {
		if (obj instanceof CompilationUnit unit) {
			render(unit);

		} else {
			appendable.append(obj.toString());
		}
	}

	public void nextLine() throws IOException {
		// formatter();
		// appendable.append(NEW_LINE);
		// for (int i = 0; i < tabs; i++) {
		//	appendable.append(TAB);
		//}
	}

	public void render(CompilationUnit unit) throws IOException {
		formatter.startObject(unit);
		render(unit.getPackage());
		nextLine();
		each(unit::getImportList, this::render, this::nextLine);
		each(unit::getClassList, this::render, this::nextLine);
		formatter.endObject(unit);
	}

	public void render(Package pkg) throws IOException {
		formatter.startObject(pkg);
		appendable.append("package ").append(pkg.getIdentifier()).append(END_OF_STATEMENT);
		formatter.endObject(pkg);
	}

	public void render(Import imp) throws IOException {
		formatter.startObject(imp);
		appendable.append("import ");
		render(imp.getModifier());
		appendable.append(' ');
		appendable.append(imp.getIdentifier()).append(END_OF_STATEMENT);
		formatter.endObject(imp);
	}

	public void render(org.xomda.lib.java.ast.Modifier mod) throws IOException {
		formatter.startObject(mod);
		if (null == mod) {
			return;
		}
		appendable.append(Modifier.toString(mod.getIdentifier().intValue()));
		formatter.endObject(mod);
	}

	public void render(Type type) throws IOException {
		formatter.startObject(type);
		appendable.append(type.getIdentifier());
		StringJoiner sj = new StringJoiner(", ", "<", ">");
		each(type::getTypeList, t -> {
			StringBuilder sb = new StringBuilder();
			render(t);
			sj.add(sb);
		});
		appendable.append(sj.toString());
		formatter.endObject(type);
	}

	public void render(Variable variable) throws IOException {
		formatter.startObject(variable);
		if (null == variable) {
			return;
		}
		appendable.append(' ').append('=').append(' ');
		appendable.append(variable.getExpression());
		formatter.endObject(variable);
	}

	public void render(Field field) throws IOException {
		formatter.startObject(field);
		each(field::getModifierList, this::render, () -> appendable.append(' '));

		appendable.append(field.getIdentifier());
		render(field.getVariable());
		appendable.append(END_OF_STATEMENT);
		formatter.endObject(field);
	}

	public void render(Class clazz) throws IOException {
		formatter.startObject(clazz);
		each(clazz::getModifierList, this::render, () -> appendable.append(' '));
		appendable.append(clazz.getModifierList() != null && clazz.getModifierList().stream().anyMatch(m -> Modifier.isInterface(m.getIdentifier().intValue()))
				? " " // written by the modifier
				: "class "
		);
		appendable.append(clazz.getIdentifier());
		appendable.append(' ');

		StringJoiner sj1 = new StringJoiner(", ", "extends ", " ");
		sj1.setEmptyValue("");
		each(clazz::getExtendsList, obj -> {
			StringBuilder sb = new StringBuilder();
			render(obj);
			sj1.add(sb);
		});
		appendable.append(sj1.toString());

		StringJoiner sj2 = new StringJoiner(", ", "implements ", " ");
		sj2.setEmptyValue("");
		each(clazz::getImplementsList, obj -> {
			StringBuilder sb = new StringBuilder();
			render(obj);
			sj1.add(sb);
		});
		appendable.append(sj2.toString());

		appendable.append('{');

		// tabs++;

		each(clazz::getClassList, c -> {
			nextLine();
			render(c);
		}, this::nextLine);

		each(clazz::getFieldList, c -> {
			nextLine();
			render(c);
		}, this::nextLine);

		each(clazz::getConstructorList, c -> {
			nextLine();
			render(c);
		}, this::nextLine);

		each(clazz::getMethodList, c -> {
			nextLine();
			render(c);
		}, this::nextLine);

		// tabs--;
		nextLine();
		appendable.append('}');
		formatter.endObject(clazz);
	}

	public void render(Constructor constructor) throws IOException {
		formatter.startObject(constructor);
		each(constructor::getModifierList, this::render, () -> appendable.append(' '));

		appendable.append(constructor.getParentClass().getIdentifier());
		appendable.append('(');
		each(constructor::getParameterList, this::render);
		appendable.append(')');

		appendable.append(' ');
		appendable.append('{');
		// tabs++;
		nextLine();

		render(constructor.getBlock());

		// tabs--;
		appendable.append('}');
		formatter.endObject(constructor);
	}

	public void render(Method method) throws IOException {
		formatter.startObject(method);

		each(method::getModifierList, this::render);

		if (method.getGenericList() != null && !method.getGenericList().isEmpty()) {
			appendable.append('<');
			StringJoiner sj = new StringJoiner(", ");
			method.getGenericList().forEach(sj::add);
			appendable.append(sj.toString());
			appendable.append('>');
			appendable.append(' ');
		}

		appendable.append(Optional.ofNullable(method.getReturntype())
						.map(Type::getIdentifier)
						.orElse("void")
				)
				.append(' ');

		appendable.append(method.getIdentifier());

		appendable.append('(');
		each(method::getParameterList, this::render);
		appendable.append(')');

		StringJoiner sj1 = new StringJoiner(", ", " throws ", " ");
		sj1.setEmptyValue("");
		each(method::getThrowsList, obj -> {
			StringBuilder sb = new StringBuilder();
			render(obj);
			sj1.add(sb);
		});
		appendable.append(sj1.toString());

		if (null == method.getParentClass() || !RenderUtils.isInterface(method.getParentClass().getModifierList())) {
			appendable.append(' ');
			appendable.append('{');
			if (null != method.getBlock()) {
				// tabs++;
				render(method.getBlock());
				// tabs--;
			}
			nextLine();
			appendable.append('}');
		} else {
			appendable.append(END_OF_STATEMENT);
		}
		formatter.endObject(method);
	}

	public void render(Block block) throws IOException {
		formatter.startObject(block);
		block.getTextList().forEach(sneakyConsumer(t -> {
			nextLine();
			formatter.startObject(t);
			appendable.append(t);
			formatter.endObject(t);
		}));
		formatter.endObject(block);
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
