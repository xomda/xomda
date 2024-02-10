package org.xomda.core.module.template;

import java.io.IOException;

import org.xomda.core.module.util.PojoWriter;
import org.xomda.model.Entity;
import org.xomda.template.TemplateContext;
import org.xomda.template.TemplateUtils;

public class GenerateEntityTemplate extends PackageTemplate {

	@Override
	public void generate(final Entity entity, final TemplateContext context) throws IOException {
		PojoWriter.createInterface(context.outDir(), TemplateUtils.getJavaInterfaceName(entity))
				.write(entity);
	}

}