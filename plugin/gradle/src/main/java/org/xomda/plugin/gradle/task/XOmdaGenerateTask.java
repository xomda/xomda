package org.xomda.plugin.gradle.task;

import static org.xomda.plugin.gradle.Constants.XOMDA_GRADLE_GROUP;
import static org.xomda.plugin.gradle.Constants.XOMDA_TASK_GENERATE_TEMPLATE_DESC;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.tasks.SourceSet;
import org.xomda.core.XOMDA;
import org.xomda.core.config.Configuration;
import org.xomda.parser.csv.CsvService;
import org.xomda.plugin.gradle.XOmdaGradlePluginExtension;
import org.xomda.plugin.gradle.util.DependencyScanner;
import org.xomda.shared.logging.LogService;

public class XOmdaGenerateTask implements Action<Task> {

	private final XOmdaGradlePluginExtension extension;

	public XOmdaGenerateTask(final SourceSet sources, final XOmdaGradlePluginExtension extension) {
		this.extension = extension;
	}

	private String getLogLevel(final Project project) {
		final LogLevel projectLogLevel = project.getLogging().getLevel();
		if (null == projectLogLevel) {
			return Level.OFF.toString();
		}
		return switch (projectLogLevel) {
			case LIFECYCLE -> Level.INFO.name();
			case QUIET -> Level.OFF.name();
			default -> projectLogLevel.name();
		};
	}

	private List<Object> readModel(final Project project, final String file) {
		try {
			final Configuration config = Configuration.builder()
					.withClassPath(extension.getClasspath().get())
					.withLogLevel(getLogLevel(project))
					.withExtensions(extension.getPlugins().get()).build();

			LogService.setLogProvider((final Class<?> clazz) -> project.getLogger());

			return new CsvService().parse(file, config);
		} catch (final IOException e) {
			project.getLogger().error("", e);
		}
		return Collections.emptyList();
	}

	private String[] getModelFiles(final Project project) {
		return extension.getModels().get().stream().map(File::new)
				.map(f -> f.isAbsolute() ? f.getAbsoluteFile() : project.file(f))
				.map(File::toString)
				.distinct()
				.toArray(String[]::new);
	}

	@Override
	public void execute(final Task task) {
		final Project project = task.getProject();
		task.setGroup(XOMDA_GRADLE_GROUP);
		task.setDescription(XOMDA_TASK_GENERATE_TEMPLATE_DESC);
		task.doLast(t -> {
			// assign the logger to XOMDA
			LogService.setLogProvider((final Class<?> clazz) -> project.getLogger());
			// a list of dependent models (from other projects), which first need to be processed
			final String[] dependentModels = DependencyScanner.getDependentModels(project);
			final String[] models = getModelFiles(project);
			project.getLogger().info("Found model definitions: {}", List.of(models));
			if (dependentModels.length > 0) {
				project.getLogger().info(" > with dependencies: {}", List.of(dependentModels));
			}

			// initialize the configuration
			final Configuration config = Configuration.builder()
					.withDependentModels(dependentModels)
					.withClassPath(extension.getClasspath().get())
					.withLogLevel(getLogLevel(project))
					.withExtensions(extension.getPlugins().get())
					.build();

			try {
				List<?> objects = XOMDA.parse(models, config);
				
				// try to execute each template with the given objects
				XOmdaCompileTask.executeTemplates(task, objects);
				// job done.
			} catch (final IOException e) {
				project.getLogger().error("", e);
			}

		});
	}

}
