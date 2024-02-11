package org.xomda.plugin.gradle.task;

import static org.xomda.plugin.gradle.Constants.XOMDA_CONFIGURATION;
import static org.xomda.plugin.gradle.Constants.XOMDA_GRADLE_GROUP;
import static org.xomda.plugin.gradle.Constants.XOMDA_TASK_COMPILE_TEMPLATES_DESC;
import static org.xomda.plugin.gradle.Constants.XOMDA_TASK_COMPILE_TEMPLATES_NAME;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.compile.JavaCompile;
import org.xomda.core.util.XOMDAUtils;
import org.xomda.plugin.gradle.util.SourceSetUtils;
import org.xomda.plugin.gradle.util.XOMDATemplateClassLoader;
import org.xomda.shared.exception.SneakyThrow;
import org.xomda.shared.util.ReflectionUtils;
import org.xomda.template.Template;
import org.xomda.template.TemplateContext;

public class XOmdaCompileTask implements Action<JavaCompile> {

	@Override
	public void execute(final JavaCompile task) {
		final Project project = task.getProject();
		final SourceSet omdaSourceSet = SourceSetUtils.getOmdaSourceSet(project);
		task.setGroup(XOMDA_GRADLE_GROUP);
		task.setDescription(XOMDA_TASK_COMPILE_TEMPLATES_DESC);
		task.setClasspath(project.files(project.getConfigurations().getAt(XOMDA_CONFIGURATION)));
		task.getDestinationDirectory().set(omdaSourceSet.getJava().getDestinationDirectory().get());
		task.setSource(omdaSourceSet.getJava().getFiles());
	}

	public static void withClassLoader(final JavaCompile compileTask, final Consumer<ClassLoader> classLoaderConsumer) {
		try (XOMDATemplateClassLoader classLoader = new XOMDATemplateClassLoader(compileTask, XOmdaCompileTask.class.getClassLoader())) {
			classLoaderConsumer.accept(classLoader);
		} catch (final IOException e) {
			compileTask.getLogger().error("", e);
		}
	}

	private static <T> void executeTemplate(final Template<T> template, final String cwd, final List<T> objects) throws IOException {
		final TemplateContext templateContext = new TemplateContext(cwd, objects);
		template.generate(objects.get(0), templateContext);
	}

	private static Stream<Template<?>> getCompiledTemplates(Task someTask, final ClassLoader classLoader) {
		final Project project = someTask.getProject();
		final JavaCompile task = (JavaCompile) project.getTasksByName(XOMDA_TASK_COMPILE_TEMPLATES_NAME, false).iterator().next();

		final Set<File> compiledClasses = task.getDestinationDirectory().get().getAsFileTree().getFiles();
		final Path taskDestinationPath = task.getDestinationDirectory().get().getAsFile().toPath();
		return compiledClasses.stream()
				.map(File::toPath)
				.map((final Path p) -> taskDestinationPath.relativize(p).toString()
						.replaceAll("\\/", ".")
						.replaceAll("\\.class$", "")
				)
				.distinct()
				.map(k -> ReflectionUtils.findClass(k, classLoader)
						.filter(XOMDAUtils::isTemplateClass)
						.map(c -> {
							try {
								return (Template<?>) c.getDeclaredConstructor().newInstance();
							} catch (final Exception e) {
								project.getLogger().error("", e);
								return null;
							}
						}))
				.filter(Optional::isPresent)
				.map(Optional::get);
	}

	public static <T> void executeTemplates(final Task someTask, final List<T> objects) {
		final Project project = someTask.getProject();
		final JavaCompile task = (JavaCompile) project.getTasksByName(XOMDA_TASK_COMPILE_TEMPLATES_NAME, false).iterator().next();
		final String cwd = project.getProjectDir().getPath();
		withClassLoader(task, (final ClassLoader cl) -> {
			getCompiledTemplates(task, cl)
					.forEach(SneakyThrow.sneaky(template -> {
						@SuppressWarnings("unchecked")
						final Template<T> t = (Template<T>) template;
						executeTemplate(t, cwd, objects);
					}));
		});

	}

}
