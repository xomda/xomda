package org.xomda.lib.java.renderer;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import org.xomda.lib.java.ast.Block;
import org.xomda.lib.java.ast.Class;
import org.xomda.lib.java.ast.CompilationUnit;
import org.xomda.lib.java.ast.Constructor;
import org.xomda.lib.java.ast.Field;
import org.xomda.lib.java.ast.Import;
import org.xomda.lib.java.ast.Method;
import org.xomda.lib.java.ast.Package;
import org.xomda.lib.java.ast.Parameter;
import org.xomda.lib.java.ast.Type;
import org.xomda.lib.java.ast.Variable;
import org.xomda.lib.java.formatter.DefaultJavaFormatter;
import org.xomda.lib.java.formatter.JavaFormatter;
import org.xomda.shared.exception.SneakyThrow;

public class JavaRenderer {

	private static final Pattern RX_NEWLINE = Pattern.compile("[\\r\\n]+");

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

	public boolean render(CompilationUnit unit) throws IOException {
		return render(unit, appendable);
	}

	public boolean render(CompilationUnit unit, Appendable appendable) throws IOException {
		formatter.startObject(unit);
		render(unit.getPackage(), appendable);
		each(appendable, unit::getImportList, this::render);
		each(appendable, unit::getClassList, this::render);
		formatter.endObject(unit);
		return true;
	}

	public boolean render(Package pkg, Appendable appendable) throws IOException {
		formatter.startObject(pkg);
		appendable.append("package ").append(pkg.getIdentifier()).append(END_OF_STATEMENT);
		formatter.endObject(pkg);
		return true;
	}

	public boolean render(Import imp, Appendable appendable) throws IOException {
		formatter.startObject(imp);
		appendable.append("import ");
		if (render(imp.getModifier(), appendable)) {
			appendable.append(SEPARATOR);
		}
		appendable.append(imp.getIdentifier()).append(END_OF_STATEMENT);
		formatter.endObject(imp);
		return true;
	}

	public boolean render(org.xomda.lib.java.ast.Modifier mod, Appendable appendable) throws IOException {
		formatter.startObject(mod);
		if (null == mod) {
			return false;
		}
		String mods = Modifier.toString(mod.getIdentifier().intValue());
		appendable.append(mods);
		formatter.endObject(mod);
		return !mods.isEmpty();
	}

	public boolean render(Type type, Appendable appendable) throws IOException {
		formatter.startObject(type);
		appendable.append(type.getIdentifier());
		each(appendable, type::getTypeList, this::render);
		formatter.endObject(type);
		return true;
	}

	public boolean render(Variable variable, Appendable appendable) throws IOException {
		if (null == variable) {
			return false;
		}
		formatter.startObject(variable);
		appendable
				.append(SEPARATOR)
				.append('=')
				.append(SEPARATOR)
				.append(variable.getExpression());
		formatter.endObject(variable);
		return true;
	}

	public boolean render(Field field, Appendable appendable) throws IOException {
		if (null == field) {
			return false;
		}
		formatter.startObject(field);

		if (each(appendable, field::getModifierList, this::render)) {
			appendable.append(SEPARATOR);
		}

		appendable.append(field.getIdentifier());
		render(field.getVariable(), appendable);
		appendable.append(END_OF_STATEMENT);
		formatter.endObject(field);

		return true;
	}

	public boolean render(Class clazz, Appendable appendable) throws IOException {
		if (null == clazz) {
			return false;
		}
		formatter.startObject(clazz);
		if (each(appendable, clazz::getModifierList, this::render)) {
			appendable.append(SEPARATOR);
		}
		appendable.append(clazz.getModifierList() != null && clazz.getModifierList().stream().anyMatch(m -> Modifier.isInterface(m.getIdentifier().intValue()))
				? " " // written by the modifier
				: "class "
		);
		appendable.append(clazz.getIdentifier());
		appendable.append(SEPARATOR);

		StringJoiner sj1 = new StringJoiner(", ", "extends ", " ");
		sj1.setEmptyValue("");
		each(appendable, clazz::getExtendsList, (obj, app) -> {
			StringBuilder sb = new StringBuilder();
			render(obj, sb);
			sj1.add(sb);
		});
		appendable.append(sj1.toString());

		StringJoiner sj2 = new StringJoiner(", ", "implements ", " ");
		sj2.setEmptyValue("");
		each(appendable, clazz::getImplementsList, (obj, app) -> {
			StringBuilder sb = new StringBuilder();
			render(obj, sb);
			sj1.add(sb);
		});
		appendable.append(sj2.toString());

		openGroup('{', appendable);
		each(appendable, clazz::getClassList, this::render);
		each(appendable, clazz::getFieldList, this::render);
		each(appendable, clazz::getConstructorList, this::render);
		each(appendable, clazz::getMethodList, this::render);
		closeGroup('}', appendable);

		formatter.endObject(clazz);

		return true;
	}

	public boolean render(Constructor constructor, Appendable appendable) throws IOException {
		if (null == constructor) {
			return false;
		}
		formatter.startObject(constructor);
		if (each(appendable, constructor::getModifierList, this::render)) {
			appendable.append(SEPARATOR);
		}

		appendable.append(constructor.getParentClass().getIdentifier());
		openGroup('(', appendable);
		each(appendable, constructor::getParameterList, this::render);
		closeGroup(')', appendable);

		appendable.append(SEPARATOR);

		openGroup('{', appendable);
		render(constructor.getBlock(), appendable);
		closeGroup('}', appendable);

		formatter.endObject(constructor);

		return true;
	}

	public boolean render(Parameter parameter, Appendable appendable) throws IOException {
		if (null == parameter) {
			return false;
		}
		if (null == parameter) {
			return false;
		}
		formatter.startObject(parameter);
		if (each(appendable, parameter::getModifierList, this::render)) {
			appendable.append(SEPARATOR);
		}
		render(parameter.getType(), appendable);
		appendable.append(parameter.getIdentifier());
		formatter.endObject(parameter);
		return true;
	}

	public boolean render(Method method, Appendable appendable) throws IOException {
		if (null == method) {
			return false;
		}
		formatter.startObject(method);

		if (each(appendable, method::getModifierList, this::render)) {
			appendable.append(SEPARATOR);
		}

		if (method.getGenericList() != null && !method.getGenericList().isEmpty()) {

			openGroup('<', appendable);
			StringJoiner sj = new StringJoiner(", ");
			method.getGenericList().forEach(sj::add);
			appendable.append(sj.toString());
			closeGroup('>', appendable);

			appendable.append(SEPARATOR);
		}

		appendable.append(Optional.ofNullable(method.getReturntype())
						.map(Type::getIdentifier)
						.orElse("void")
				)
				.append(SEPARATOR);

		appendable.append(method.getIdentifier());

		appendable.append('(');
		each(appendable, method::getParameterList, this::render);
		appendable.append(')');

		StringJoiner sj1 = new StringJoiner(", ", " throws ", " ");
		sj1.setEmptyValue("");
		each(appendable, method::getThrowsList, (obj, app) -> {
			StringBuilder sb = new StringBuilder();
			render(obj, sb);
			sj1.add(sb);
		});
		appendable.append(sj1.toString());

		if (null == method.getParentClass() || !RenderUtils.isInterface(method.getParentClass().getModifierList())) {
			appendable.append(SEPARATOR);

			openGroup('{', appendable);
			if (null != method.getBlock()) {
				render(method.getBlock(), appendable);
			}
			closeGroup('}', appendable);

		} else {
			appendable.append(END_OF_STATEMENT);
		}
		formatter.endObject(method);

		return true;
	}

	public boolean render(String obj, Appendable appendable) throws IOException {
		if (null == obj || obj.isEmpty()) {
			return false;
		}
		String[] lines = RX_NEWLINE.split(obj);
		for (String line : lines) {
			formatter.startObject(line);
			appendable.append(line);
			formatter.endObject(line);
		}
		return true;
	}

	public boolean render(Block block, Appendable appendable) throws IOException {
		if (null == block) {
			return false;
		}
		formatter.startObject(block);
		each(appendable, block::getTextList, this::render);
		formatter.endObject(block);
		return true;
	}

	private void openGroup(char c, Appendable appendable) throws IOException {
		appendable.append(c);
		formatter.startGroup(c);
	}

	private void closeGroup(char c, Appendable appendable) throws IOException {
		formatter.endGroup(c);
		appendable.append(c);
	}

	private <T, E extends Throwable> boolean each(Appendable appendable, Supplier<Collection<T>> supplier, SneakyThrow.ThrowingBiConsumer<T, Appendable, E> consumer) {
		Collection<T> col = supplier.get();
		if (null == col || col.isEmpty()) {
			return false;
		}
		col.forEach(c -> consumer.accept(c, appendable));
		return true;
	}

}
