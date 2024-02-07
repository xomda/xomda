package org.xomda.core.java.template;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.xomda.core.java.JavaUtils;

public class JavaClassWriter extends JavaTemplateContext {

	private List<String> headers = List.of();

	public JavaClassWriter(final String className) {
		super(className, new ByteArrayOutputStream());
	}

	/**
	 * @see JavaUtils#isReserved(String)
	 */
	public boolean isReserved(final String word) {
		return JavaUtils.isReserved(word);
	}

	public JavaClassWriter withHeaders(final String... headers) {
		return withHeaders(null == headers ? Collections.emptyList() : Arrays.asList(headers));
	}

	public JavaClassWriter withHeaders(final List<String> headers) {
		this.headers = headers;
		return this;
	}

	public JavaClassWriter withHeader(final String header) {
		return withHeaders(header);
	}

	public void writeFile(final String root) throws IOException {
		writeFile(Paths.get(root));
	}

	public void writeFile(final Path root) throws IOException {
		final ByteArrayOutputStream bos = (ByteArrayOutputStream) getOutputStream();
		final String className = getFullClassName();

		final Path outFile = root.resolve(JavaUtils.toJavaPath(className));
		Files.createDirectories(outFile.getParent());

		bos.flush();
		getWriter().flush();

		final List<String> imports = getImports();

		try (
				final OutputStream os = Files.newOutputStream(outFile);
				final PrintStream ps = new PrintStream(os, false, StandardCharsets.UTF_8);
		) {
			if (null != headers) {
				headers.forEach(ps::println);
			}
			ps.println("package " + getPackageName() + ";");
			ps.println();
			if (!imports.isEmpty()) {
				imports.forEach(imp -> {
					if (imp.isEmpty()) {
						ps.println();
					} else {
						ps.println(imp);
					}
				});
				ps.println();
			}
			bos.writeTo(os);
		}
	}

}
