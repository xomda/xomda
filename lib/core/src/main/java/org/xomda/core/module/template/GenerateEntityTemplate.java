package org.xomda.core.module.template;

import static java.util.function.Predicate.not;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.xomda.core.java.JavaClassWriter;
import org.xomda.core.java.util.GetterSetter;
import org.xomda.core.util.XOMDAUtils;
import org.xomda.model.Attribute;
import org.xomda.model.Entity;
import org.xomda.shared.util.StringUtils;
import org.xomda.template.TemplateContext;
import org.xomda.template.TemplateUtils;

public class GenerateEntityTemplate extends PackageTemplate {

	@Override
	public void generate(final Entity entity, final TemplateContext context) throws IOException {
		try (
				@SuppressWarnings("resource") final JavaClassWriter ctx = new JavaClassWriter(TemplateUtils.getJavaInterfaceName(entity))
						.withHeaders("// THIS FILE WAS AUTOMATICALLY GENERATED", "");
		) {
			ctx
					.addDocs(docs -> Optional
							.ofNullable(entity.getDescription())
							.filter(not(String::isBlank))
							.ifPresent(docs::println)
					)
					.println("public interface {0} {", ctx.getClassName()).tab(tabbed -> tabbed
							.println()
							// generate the regular attributes
							.forEach(entity::getAttributeList, (final Attribute attribute) -> {
								final String attributeName = StringUtils.toPascalCase(attribute.getName());
								final String fullyQualifiedType = ctx.addImport(TemplateUtils.getJavaType(attribute));
								GetterSetter.create(fullyQualifiedType, attributeName)
										.declareOnly()
										.withJavaDoc(attribute.getDescription())
										.writeTo(tabbed);
							})
							// generate the reverse entity attributes
							.forEach(XOMDAUtils.findReverseEntities(entity), (final Entity e) -> {
								final String attributeName = StringUtils.toPascalCase(e.getName() + " List");
								final CharSequence fullyQualifiedType = ctx.addGenericImport(
										List.class,
										TemplateUtils.getJavaType(e)
								);
								GetterSetter.create(fullyQualifiedType, attributeName)
										.declareOnly()
										.withJavaDoc(entity.getDescription())
										.writeTo(tabbed);
							}))
					.println("}");
			ctx.writeFile(context.outDir());
		}
	}

}