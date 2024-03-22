package org.xomda.lib.java;

import java.io.IOException;
import java.nio.file.Paths;

import org.xomda.core.module.template.PackageTemplate;
import org.xomda.core.module.util.EnumWriter;
import org.xomda.core.module.util.PojoWriter;
import org.xomda.model.Entity;
import org.xomda.template.TemplateContext;
import org.xomda.template.TemplateUtils;

public class TestModelTemplate extends PackageTemplate {

	@Override
	public void generate(final org.xomda.model.Package pkg, final TemplateContext context) throws IOException {
		getLogger().info("Generating multi-model (" + pkg.getName() + ")");
		super.generate((org.xomda.model.Package) pkg, context);
	}

	@Override
	public void generate(final Entity entity, final TemplateContext context) throws IOException {
		String newPath = Paths.get(context.cwd(), "src", "generated", "java").toString();
		TemplateContext newContext = new TemplateContext(newPath, context.getParseResults());

		String javaInterface = TemplateUtils.getJavaInterfaceName(entity);
		String javaClass = TemplateUtils.getJavaBeanName(entity);
		PojoWriter
				.createInterface(newContext.cwd(), javaInterface)
				.write(entity);
		PojoWriter
				.create(newContext.cwd(), javaClass)
				.withImplements(javaInterface)
				.withExtends(entity.getParent() != null ? new String[] { TemplateUtils.getJavaBeanName(entity.getParent()) } : new String[0])
				.write(entity);
	}

	@Override
	public void generate(final org.xomda.model.Enum enm, final TemplateContext context) throws IOException {
		EnumWriter.create(context.cwd(), TemplateUtils.getJavaEnumName(enm))
				.write(enm);
	}
}
