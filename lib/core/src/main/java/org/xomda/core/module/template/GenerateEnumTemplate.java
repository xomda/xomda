package org.xomda.core.module.template;

import java.io.IOException;

import org.xomda.core.module.util.EnumWriter;
import org.xomda.template.TemplateContext;
import org.xomda.template.TemplateUtils;

public class GenerateEnumTemplate extends BasePackageTemplate {

	@Override
	public void generate(final org.xomda.model.Enum enm, final TemplateContext context) throws IOException {
		EnumWriter.create(getJavaSrcDir(context), TemplateUtils.getJavaEnumName(enm))
				.withBanner("// THIS FILE WAS AUTOMATICALLY GENERATED", "")
				.write(enm);
	}

}
