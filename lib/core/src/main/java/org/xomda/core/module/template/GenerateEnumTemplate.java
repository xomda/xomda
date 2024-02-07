package org.xomda.core.module.template;

import java.io.IOException;

import org.xomda.core.template.TemplateContext;
import org.xomda.core.template.TemplateUtils;
import org.xomda.core.template.context.java.JavaClassWriter;
import org.xomda.model.Value;
import org.xomda.shared.util.StringUtils;

public class GenerateEnumTemplate extends org.xomda.core.module.template.PackageTemplate {

	@Override
	public void generate(final org.xomda.model.Enum enm, final TemplateContext context) throws IOException {
		try (
				@SuppressWarnings("resource")
				final JavaClassWriter ctx = new JavaClassWriter(TemplateUtils.getJavaEnumName(enm))
						.withHeaders("// THIS FILE WAS AUTOMATICALLY GENERATED", "");
		) {
			ctx
					.println("public enum {0} {", ctx.getClassName())
					.tab(tabbed -> tabbed.forEach(enm::getValueList,
							(final Value value) -> tabbed.println("{0}, ", StringUtils.toPascalCase(value.getName()))))
					.println("}");

			ctx.writeFile(context.outDir());
		}
	}

}
