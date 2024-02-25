package org.xomda.core.module.util;

import static java.util.function.Predicate.not;
import static org.xomda.shared.exception.SneakyThrow.sneaky;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.xomda.core.java.DeferredContext;
import org.xomda.core.java.JavaClassWriter;
import org.xomda.core.java.JavaTemplateContext;
import org.xomda.core.java.util.GetterSetter;
import org.xomda.core.util.XOMDAUtils;
import org.xomda.model.Attribute;
import org.xomda.model.Entity;
import org.xomda.shared.util.StringUtils;
import org.xomda.template.TemplateUtils;

/**
 * Helper to write java POJOs.
 * It can either write an interface or the full class, depending on the "declareOnly" parameter.
 */
public class PojoWriter {

	private String outputPath;
	private String className;
	private boolean declareOnly;
	private List<String> banner;

	private final List<String> extendList = new ArrayList<>();
	private final List<String> implementList = new ArrayList<>();

	public String getClassName() {
		return className;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public PojoWriter withExtends(String... ext) {
		extendList.addAll(Arrays.asList(ext));
		return this;
	}

	public PojoWriter withImplements(String... ext) {
		implementList.addAll(Arrays.asList(ext));
		return this;
	}

	public PojoWriter withBanner(String... ext) {
		this.banner = List.of(ext);
		return this;
	}

	public boolean getDeclareOnly() {
		return declareOnly;
	}

	private String getClassExtensions(JavaClassWriter ctx) {
		Set<String> extendList = new LinkedHashSet<>(this.extendList);
		Set<String> implementList = new LinkedHashSet<>(this.implementList);
		if (extendList.isEmpty() && implementList.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		if (!extendList.isEmpty()) {
			sb.append(" extends ").append(extendList.stream()
					.map(ctx::addImport)
					.collect(Collectors.joining(", "))
			);
		}
		if (!implementList.isEmpty()) {
			sb.append(" implements ").append(implementList.stream()
					.map(ctx::addImport)
					.collect(Collectors.joining(", "))
			);
		}
		return sb.toString();
	}

	private void addInterfaceAndSuperClass(Entity entity) {
		if (!declareOnly) {
			return;
		}

		if (null != entity.getParent()) {
			extendList.add(TemplateUtils.getJavaInterfaceName(entity));
		}
	}

	public void write(final Entity entity) throws IOException {
		boolean declareOnly = getDeclareOnly();
		addInterfaceAndSuperClass(entity);
		String type = declareOnly ? "interface" : "class";
		try (final JavaClassWriter ctx = new JavaClassWriter(getClassName())) {
			ctx
					.withHeaders(banner)
					.withJavaDoc(docs -> Optional
							.ofNullable(entity.getDescription())
							.filter(not(String::isBlank))
							.ifPresent(docs::printlnEscaped)
					)
					.println("public {0} {1}{2} {", type, ctx.getClassName(), getClassExtensions(ctx)).tab(sneaky(tabbed -> {
						final DeferredContext globalContext = (DeferredContext) tabbed.deferred();
						final JavaTemplateContext bodyContext = tabbed.deferred();
						tabbed
								.println()
								// generate the regular attributes
								.forEach(entity::getAttributeList, (final Attribute attribute) -> {
									final String attributeName = StringUtils.toPascalCase(attribute.getName());
									final String fullyQualifiedType = ctx.addImport(TemplateUtils.getJavaType(attribute));
									GetterSetter.create(fullyQualifiedType, attributeName)
											.declareOnly(declareOnly)
											.withModifiers(Modifier.PUBLIC)
											.withJavaDoc(attribute.getDescription())
											.writeTo(globalContext, bodyContext);
								})
								// generate the reverse entity attributes
								.forEach(XOMDAUtils.findReverseEntities(entity), (final Entity e) -> {
									final String attributeName = StringUtils.toPascalCase(e.getName() + " List");
									final CharSequence fullyQualifiedType = ctx.addGenericImport(
											List.class,
											TemplateUtils.getJavaType(e)
									);
									GetterSetter.create(fullyQualifiedType, attributeName)
											.declareOnly(declareOnly)
											.withModifiers(Modifier.PUBLIC)
											.withJavaDoc(entity.getDescription())
											.writeTo(globalContext, bodyContext);
								});

						// flush the deferred contexts
						if (!globalContext.isEmpty()) {
							globalContext.println();
							globalContext.flush();
						}
						bodyContext.flush();
					}))
					.println("}");
			ctx.writeFile(getOutputPath());
		}
	}

	public static PojoWriter create(String outputPath, String className) {
		PojoWriter instance = new PojoWriter();
		instance.outputPath = outputPath;
		instance.className = className;
		return instance;
	}

	public static PojoWriter createInterface(String outputPath, String className) {
		PojoWriter instance = create(outputPath, className);
		instance.declareOnly = true;
		return instance;
	}

}
