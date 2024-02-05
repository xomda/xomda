package org.xomda.plugin.gradle;

import static org.xomda.core.Constants.XOMDA_GROUP;
import static org.xomda.plugin.gradle.Constants.XOMDA_CONFIGURATION;
import static org.xomda.plugin.gradle.Constants.XOMDA_TASK_COMPILE_TEMPLATES;
import static org.xomda.plugin.gradle.Constants.XOMDA_TASK_GENERATE_TEMPLATE_NAME;

import java.io.File;
import java.util.Set;
import java.util.stream.Stream;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.compile.JavaCompile;
import org.xomda.core.config.Configuration;
import org.xomda.plugin.gradle.task.XOmdaCompileTemplatesTask;
import org.xomda.plugin.gradle.task.XOmdaProcessModelsTask;
import org.xomda.plugin.gradle.util.SourceSetUtils;
import org.xomda.shared.logging.LogService;

/**
 * The XOMDA Gradle plugin
 */
@SuppressWarnings("unused")
public class XOmdaGradlePlugin implements Plugin<Project> {

	public void apply(Project project) {

		// build config
		project.getPluginManager().apply(JavaPlugin.class);

		// Define the "xomda" configuration
		org.gradle.api.artifacts.Configuration conf = project.getConfigurations().register(XOMDA_CONFIGURATION).get();

		// add XOMDA Core dependency to the "xomda" configuration
		// the version should be the same as the version of the plugin
		project.getBuildscript().getConfigurations().stream().flatMap(c -> c.getDependencies().stream())
				.filter((Dependency dep) -> dep.getName().startsWith(XOMDA_GROUP)).findFirst().ifPresent(d -> {
					Stream.of(XOMDA_GROUP + ":core", XOMDA_GROUP + ":api").map(s -> s + ":" + d.getVersion())
							.forEach(xomdaDependency -> {
								Dependency dep = project.getDependencies().create(xomdaDependency);
								project.getDependencies().add(conf.getName(), dep);
							});
				});

		// Create a custom source set
		SourceSet omdaSourceSet = SourceSetUtils.createOmdaSourceSet(project);

		// Inherit dependencies
		Stream.of(omdaSourceSet.getCompileClasspathConfigurationName(),
						omdaSourceSet.getRuntimeClasspathConfigurationName()).map(project.getConfigurations()::getAt)
				.forEach(confImp -> confImp.extendsFrom(conf));

		// plugin extension (config)
		XOmdaGradlePluginExtension extension = project.getExtensions().create(Constants.XOMDA_CONFIGURATION,
				XOmdaGradlePluginExtension.class);
		extension.getClasspath().convention(Set.of(Configuration.DEFAULT_CLASSPATH));
		extension.getModels().convention(omdaSourceSet.getResources().getFiles().stream().map(File::toString).toList());

		// logger
		LogService.setLogProvider((Class<?> clazz) -> project.getLogger());

		// define tasks
		project.getTasks().register(XOMDA_TASK_GENERATE_TEMPLATE_NAME,
				new XOmdaProcessModelsTask(omdaSourceSet, extension));
		project.getTasks().register(XOMDA_TASK_COMPILE_TEMPLATES, JavaCompile.class, new XOmdaCompileTemplatesTask());

		// add to build task
		Task buildTask = project.getTasks().getAt("build");
		buildTask.dependsOn(XOMDA_TASK_COMPILE_TEMPLATES, XOMDA_TASK_GENERATE_TEMPLATE_NAME);
	}

}
