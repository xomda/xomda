package org.xomda.core.module.template;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.xomda.core.template.TemplateContext;
import org.xomda.core.template.TemplateUtils;
import org.xomda.core.template.context.java.JavaTemplateContext;
import org.xomda.model.Value;
import org.xomda.shared.util.StringUtils;

public class GenerateEnumTemplate extends org.xomda.core.module.template.PackageTemplate {

	public void generate(org.xomda.model.Enum enm, TemplateContext context) throws IOException {
		final Path root = Paths.get(context.outDir());
		final String pkg = TemplateUtils.getJavaPackage(enm.getPackage());
		final String enumName = StringUtils.toPascalCase(enm.getName());
		final String fullyQualifiedName = pkg + "." + enumName;

		// find out and create the target directory
		final Path outFile = root.resolve(TemplateUtils.getEnumPath(enm));
		Files.createDirectories(outFile.getParent());

		try (final FileOutputStream fos = new FileOutputStream(outFile.toFile());
				final BufferedOutputStream bos = new BufferedOutputStream(fos);
				final JavaTemplateContext ctx = new JavaTemplateContext(fullyQualifiedName, bos)) {
			ctx.println("// THIS FILE WAS AUTOMATICALLY GENERATED").println().println("package " + pkg + ";").println()
					.println("public enum " + enumName + " {")
					.tab(tabbed -> tabbed.forEach(enm::getValueList,
							(Value value) -> tabbed.println("{0}, ", StringUtils.toPascalCase(value.getName()))))
					.println("}");
		}
	}

}
