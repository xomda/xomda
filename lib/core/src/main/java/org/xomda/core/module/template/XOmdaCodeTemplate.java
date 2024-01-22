package org.xomda.core.module.template;

import java.io.IOException;

import org.xomda.core.template.TemplateContext;
import org.xomda.model.Entity;

public class XOmdaCodeTemplate extends org.xomda.core.module.template.PackageTemplate {

    private final org.xomda.core.module.template.GenerateEnumTemplate enumTemplate = new org.xomda.core.module.template.GenerateEnumTemplate();
    private final org.xomda.core.module.template.GenerateEntityTemplate entityTemplate = new org.xomda.core.module.template.GenerateEntityTemplate();

    @Override
    public void generate(final org.xomda.model.Enum enm, final TemplateContext context) throws IOException {
        enumTemplate.generate(enm, context);
    }

    @Override
    public void generate(final Entity entity, final TemplateContext context) throws IOException {
        entityTemplate.generate(entity, context);
    }
}
