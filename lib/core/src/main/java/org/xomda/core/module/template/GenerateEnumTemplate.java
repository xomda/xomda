package org.xomda.core.module.template;

import static java.util.function.Predicate.not;

import java.io.IOException;
import java.util.Optional;

import org.xomda.core.java.JavaClassWriter;
import org.xomda.model.Value;
import org.xomda.shared.util.StringUtils;
import org.xomda.template.TemplateContext;
import org.xomda.template.TemplateUtils;

public class GenerateEnumTemplate extends PackageTemplate {

	@Override
	public void generate(final org.xomda.model.Enum enm, final TemplateContext context) throws IOException {
		try (
				@SuppressWarnings("resource")
				final JavaClassWriter ctx = new JavaClassWriter(TemplateUtils.getJavaEnumName(enm))
						.withHeaders("// THIS FILE WAS AUTOMATICALLY GENERATED", "")
        ) {
			ctx
					.addDocs(docs -> Optional
							.ofNullable(enm.getDescription())
							.filter(not(String::isBlank))
							.ifPresent(docs::printlnEscaped)
					)
					.println("public enum {0} {", ctx.getClassName())
					.tab(tabbed -> tabbed.forEach(
							enm::getValueList,
							(final Value value) -> tabbed.println("{0}, ", StringUtils.toPascalCase(value.getName()))
					))
					.println("}");

			ctx.writeFile(context.outDir());
		}
	}

}
