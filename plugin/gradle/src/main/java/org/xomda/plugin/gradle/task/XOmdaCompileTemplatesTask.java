package org.xomda.plugin.gradle.task;

import static org.xomda.plugin.gradle.Constants.XOMDA_CONFIGURATION;
import static org.xomda.plugin.gradle.Constants.XOMDA_TASK_COMPILE_TEMPLATES;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.compile.JavaCompile;
import org.xomda.core.template.Template;
import org.xomda.core.template.TemplateContext;
import org.xomda.plugin.gradle.util.SourceSetUtils;
import org.xomda.shared.util.ReflectionUtils;

public class XOmdaCompileTemplatesTask implements Action<JavaCompile> {

	public void execute(JavaCompile task) {
		Project project = task.getProject();
		SourceSet omdaSourceSet = SourceSetUtils.getOmdaSourceSet(project);

		task.setClasspath(project.files(project.getConfigurations().getAt(XOMDA_CONFIGURATION)));

		task.getDestinationDirectory().set(omdaSourceSet.getJava().getDestinationDirectory().get());
		task.setSource(omdaSourceSet.getJava().getFiles());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Class<Template>> getUserClasses(Task someTask) {
		Project project = someTask.getProject();
		JavaCompile task = (JavaCompile) project.getTasksByName(XOMDA_TASK_COMPILE_TEMPLATES, false).iterator().next();

		Set<File> compiledClasses = task.getDestinationDirectory().get().getAsFileTree().getFiles();
		Path taskDestinationPath = task.getDestinationDirectory().get().getAsFile().toPath();
		Map<String, URL> map = compiledClasses.stream().map(File::toPath)
				.collect(Collectors.toMap((Path p) -> taskDestinationPath.relativize(p).toString()
						.replaceAll("\\/", ".").replaceAll("\\.class$", ""), (Path p) -> {
							try {
								return p.toUri().toURL();
							} catch (MalformedURLException e) {
								throw new RuntimeException(e);
							}
						}));

		try {
			URL[] deps = Stream.concat(Stream.of(task.getDestinationDirectory().get().getAsFile().toURI().toURL()),
					project.files(project.getConfigurations().getAt(XOMDA_CONFIGURATION)).getFiles().stream().map(f -> {
						try {
							return f.toURI().toURL();
						} catch (MalformedURLException e) {
							project.getLogger().error("", e);
							return null;
						}
					}).filter(Objects::nonNull)).toArray(URL[]::new);

			try (URLClassLoader cl = new URLClassLoader(deps, XOmdaCompileTemplatesTask.class.getClassLoader())) {
				return map.keySet().stream().map(k -> ReflectionUtils.findClass(k, cl)).filter(Optional::isPresent)
						.map(Optional::get)
//                    .filter(ReflectionUtils.extendsFrom(Template.class))
						.filter(Template.class::isAssignableFrom).map(clz -> (Class<Template>) (Class) clz).toList();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> List<Consumer<T>> getTemplates(Task task) {
		String cwd = task.getProject().getProjectDir().getPath();
		return getUserClasses(task).stream().map(clazz -> (Consumer<T>) (T t) -> {
			try {
				TemplateContext templateContext = new TemplateContext(cwd);
				@SuppressWarnings("unchecked")
				Template<T> c = clazz.getDeclaredConstructor().newInstance();
				c.generate(t, templateContext);
			} catch (IOException | InstantiationException | IllegalAccessException | InvocationTargetException
					| NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		}).toList();
	}

}
