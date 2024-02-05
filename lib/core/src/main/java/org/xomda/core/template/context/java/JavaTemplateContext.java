package org.xomda.core.template.context.java;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.xomda.core.java.JavaImportService;
import org.xomda.core.java.JavaUtils;
import org.xomda.core.template.context.DefaultContext;

public class JavaTemplateContext implements DefaultContext<JavaTemplateContext>, Closeable, Flushable {

	private final JavaImportService importService;

	private final OutputStream outputStream;
	private final PrintWriter writer;
	private final String localClassName;
	private String tabCharacter = TabbedContext.DEFAULT_TAB_CHARACTER;

	private boolean newLine = true;

	JavaTemplateContext(final String localClassName, final OutputStream out) {
		this.localClassName = localClassName;
		outputStream = out;
		writer = new PrintWriter(out);
		importService = new JavaImportService(localClassName);
	}

	JavaTemplateContext(final JavaTemplateContext parent) {
		localClassName = parent.localClassName;
		outputStream = parent.outputStream;
		writer = parent.writer;
		tabCharacter = parent.tabCharacter;
		importService = parent.importService;
	}

	public String getClassName() {
		return localClassName;
	}

	public String getPackageName() {
		return JavaUtils.getPackageName(localClassName);
	}

	public String addImport(final String className) {
		return importService.addImport(className);
	}

	public String addImport(final Class<?> clazz) {
		return importService.addImport(clazz);
	}

	public String addStaticImport(final String methodName) {
		return importService.addStaticImport(methodName);
	}

	public String addStaticImport(final Class<?> clazz, final String methodName) {
		return importService.addStaticImport(clazz, methodName);
	}

	public List<String> getImports() {
		final List<String> imports = new ArrayList<>();
		importService.forEach((final String imp) -> imports.add("import " + imp + ";"), () -> imports.add(""));
		return imports;
	}

	public Stream<String> imports() {
		return getImports().stream();
	}

	public int getTabCount() {
		return 0;
	}

	public String getSingleTab() {
		return tabCharacter;
	}

	public void setTabCharacter(final String character) {
		tabCharacter = character;
	}

	protected OutputStream getOutputStream() {
		return outputStream;
	}

	@Override
	public PrintWriter getWriter() {
		return writer;
	}

	@Override
	public JavaTemplateContext deferred() {
		return new DeferredContext(this);
	}

	@Override
	public JavaTemplateContext tab(final int count) {
		return new TabbedContext(this, count);
	}

	public JavaTemplateContext addDocs(final Consumer<JavaDocWriter> docWriterConsumer) {
		try (JavaDocWriter docCtx = new JavaDocWriter(this);) {
			docWriterConsumer.accept(docCtx);
		}
		return this;
	}

	@Override
	public boolean isNewLine() {
		return newLine;
	}

	@Override
	public void setNewLine(final boolean state) {
		newLine = state;
	}

	@Override
	public void flush() throws IOException {
		getWriter().flush();
	}

	@Override
	public void close() throws IOException {
		flush();
		getWriter().close();
	}

	public static JavaTemplateContext create(final String localClassName, final OutputStream out) {
		return new JavaTemplateContext(localClassName, out);
	}

}
