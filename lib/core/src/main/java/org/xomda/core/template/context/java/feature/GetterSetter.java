package org.xomda.core.template.context.java.feature;

import java.lang.reflect.Modifier;
import java.util.Objects;

import org.xomda.core.template.TemplateUtils;
import org.xomda.core.template.context.java.JavaTemplateContext;
import org.xomda.shared.util.StringUtils;

public class GetterSetter {

	private String javadoc;
	private String name;
	private String type;
	private int modifiers;

	private boolean getOnly;
	private boolean setOnly;
	private boolean withBody = true;

	public GetterSetter withName(final String name) {
		this.name = name;
		return this;
	}

	public GetterSetter withJavaDoc(final String javadoc) {
		this.javadoc = javadoc;
		return this;
	}

	public GetterSetter withType(final String type) {
		this.type = type;
		return this;
	}

	public GetterSetter withModifiers(final int... modifiers) {
		int mod = 0;
		for (final int modifier : modifiers) {
			mod = mod & modifier;
		}

		this.modifiers = mod;
		return this;
	}

	public GetterSetter declareOnly(final boolean declareOnly) {
		withBody = !declareOnly;
		return this;
	}

	public GetterSetter declareOnly() {
		return declareOnly(true);
	}

	public GetterSetter getOnly(final boolean getOnly) {
		this.getOnly = getOnly;
		return this;
	}

	public GetterSetter getOnly() {
		return getOnly(true);
	}

	public GetterSetter setOnly(final boolean setOnly) {
		this.setOnly = setOnly;
		return this;
	}

	public GetterSetter setOnly() {
		return setOnly(true);
	}

	public void writeTo(final JavaTemplateContext globalsCtx, final JavaTemplateContext ctx) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(type);

		final String getterName = "get" + StringUtils.toUpper1(name);
		final String setterName = "set" + StringUtils.toUpper1(name);
		final String variableName = TemplateUtils.getJavaIdentifier(StringUtils.toCamelCase(name));
		final String type = ctx.addImport(this.type);
		final String fnModifiers = withBody ? Modifier.toString(modifiers) + (modifiers == 0 ? "" : " ") : "";
		final boolean isStatic = Modifier.isStatic(modifiers);
		final boolean hasJavaDoc = !StringUtils.isNullOrBlank(javadoc);

		if (withBody) {
			final String modifiers = "private" + (isStatic ? " static" : "");
			// declare the variable
			globalsCtx.println("{0} {1} {2};", modifiers, type, variableName);
			if (globalsCtx == ctx) {
				globalsCtx.println();
			}
		}

		if (!setOnly) {
			ctx
					.addDocs(doc -> {
						if (hasJavaDoc) {
							doc.println(javadoc);
						}
					})
					.print("{0}{1} {2}()", fnModifiers, type, getterName);

			if (!withBody) {
				ctx.println(";");
			} else {
				ctx
						.println(" {").tab(tabbed -> tabbed
								.println("return {0};", variableName))
						.println("}");
			}
			ctx.println();
		}

		if (!getOnly) {
			ctx
					.addDocs(doc -> {
						if (hasJavaDoc) {
							doc.println(javadoc);
						}
					})
					.print("{0}void {1}(final {2} {3})", fnModifiers, setterName, type, variableName);

			if (!withBody) {
				ctx.println(";");
			} else {
				ctx
						.println(" {").tab(tabbed -> tabbed
								.println("this.{0} = {0};", variableName))
						.println("}");
			}
			ctx.println();
		}

	}

	public void writeTo(final JavaTemplateContext ctx) {
		writeTo(ctx, ctx);
	}

	public static GetterSetter create(final String type, final String name) {
		return new GetterSetter().withType(type).withName(name);
	}

}
