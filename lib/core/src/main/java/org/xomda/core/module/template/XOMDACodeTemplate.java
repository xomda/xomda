package org.xomda.core.module.template;

import java.io.IOException;

import org.xomda.model.Entity;
import org.xomda.template.TemplateContext;

public class XOMDACodeTemplate extends BasePackageTemplate {

	private final GenerateEnumTemplate enumTemplate = new GenerateEnumTemplate();
	private final GenerateEntityTemplate entityTemplate = new GenerateEntityTemplate();

	@Override
	public void generate(final org.xomda.model.Enum enm, final TemplateContext context) throws IOException {
		enumTemplate.generate(enm, context);
	}

	@Override
	public void generate(final Entity entity, final TemplateContext context) throws IOException {
		entityTemplate.generate(entity, context);
	}
}
