package org.xomda.core.module.util;

import static java.util.function.Predicate.not;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.xomda.core.java.JavaClassWriter;
import org.xomda.core.java.JavaUtils;
import org.xomda.model.Value;

/**
 * Helper to write java enums.
 */
public class EnumWriter {

	private String outputPath;
	private String className;
	private List<String> banner;

	private final List<String> implementList = new ArrayList<>();

	public String getClassName() {
		return className;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public EnumWriter withImplements(String... ext) {
		implementList.addAll(Arrays.asList(ext));
		return this;
	}

	public EnumWriter withBanner(String... ext) {
		this.banner = List.of(ext);
		return this;
	}

	private String getClassExtensions(JavaClassWriter ctx) {
		return implementList.isEmpty()
				? ""
				: " implements " + implementList.stream()
						.map(ctx::addImport)
						.collect(Collectors.joining(", "));
	}

	public void write(final org.xomda.model.Enum enm) throws IOException {
		try (final JavaClassWriter ctx = new JavaClassWriter(getClassName())) {
			ctx
					.withHeaders(banner)
					.addDocs(docs -> Optional
							.ofNullable(enm.getDescription())
							.filter(not(String::isBlank))
							.ifPresent(docs::printlnEscaped)
					)
					.println("public enum {0}{1} {", ctx.getClassName(), getClassExtensions(ctx))
					.tab(tabbed -> tabbed.forEach(
							enm::getValueList,
							(final Value value) -> tabbed.println("{0}, ", JavaUtils.toEnumValue(value.getName()))
					))
					.println("}");
			ctx.writeFile(getOutputPath());
		}
	}

	public static EnumWriter create(String outputPath, String className) {
		EnumWriter instance = new EnumWriter();
		instance.outputPath = outputPath;
		instance.className = className;
		return instance;
	}

}
