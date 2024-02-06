package org.xomda.plugin.gradle.task;

import static org.xomda.core.Constants.XOMDA_GROUP;
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
import org.xomda.core.config.Configuration;
import org.xomda.core.csv.CsvService;
import org.xomda.plugin.gradle.XOmdaGradlePluginExtension;
import org.xomda.shared.logging.LogService;

public class XOmdaProcessModelsTask implements Action<Task> {

	private final XOmdaGradlePluginExtension extension;

	public XOmdaProcessModelsTask(SourceSet sources, XOmdaGradlePluginExtension extension) {
		this.extension = extension;
	}

	private String getLogLevel(Project project) {
		final LogLevel projectLogLevel = project.getLogging().getLevel();
		if (null == projectLogLevel)
			return Level.OFF.toString();
		return switch (projectLogLevel) {
			case LIFECYCLE -> Level.INFO.name();
			case QUIET -> Level.OFF.name();
			default -> projectLogLevel.name();
		};
	}

	private List<Object> readModel(Project project, String file) {
		try {
			Configuration config = Configuration.builder()
					.withClassPath(extension.getClasspath().get())
					.withLogLevel(getLogLevel(project))
					.withExtensions(extension.getPlugins().get()).build();

			LogService.setLogProvider((Class<?> clazz) -> project.getLogger());

			return new CsvService().read(file, config);
		} catch (IOException e) {
			project.getLogger().error("", e);
		}
		return Collections.emptyList();
	}

	private List<String> getModelFiles(Project project) {
		return extension.getModels().get().stream().map(File::new)
				.map(f -> f.isAbsolute() ? f.getAbsoluteFile() : project.file(f)).map(File::toString).toList();
	}

	@Override
	public void execute(Task task) {
		Project project = task.getProject();
		task.setGroup(XOMDA_GROUP);
		task.setDescription(XOMDA_TASK_GENERATE_TEMPLATE_DESC);
		task.doLast(t -> {
			List<String> files = getModelFiles(project);
			project.getLogger().info("Found model definitions: {}", files);
			files.forEach((String file) -> {
				List<Object> objects = readModel(project, file);
				XOmdaCompileTemplatesTask.executeTemplates(task, objects);
			});
		});
	}

}
