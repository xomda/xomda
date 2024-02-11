package org.xomda.plugin.gradle.util;

import java.util.stream.Stream;

import org.gradle.api.Project;
import org.gradle.api.artifacts.ProjectDependency;
import org.xomda.plugin.gradle.XOmdaGradlePluginExtension;

public class XOMDADepencyScanner {

	static Stream<Project> scanDepsPost(Project project) {
		return project.getConfigurations().stream().flatMap(configuration -> configuration.getDependencies()
				.stream()
				.filter(ProjectDependency.class::isInstance)
				.flatMap(dependency -> {
					ProjectDependency projectDependency = (ProjectDependency) dependency;
					// Check if the dependency is a project dependency
					Project dependentProject = projectDependency.getDependencyProject();

					return Stream.concat(
							Stream.of(dependentProject),
							scanDepsPost(dependentProject)
					);
				}));
	}

	public static void scanDeps(Project project) {
		project.getLogger().lifecycle("DEP SCAN");
		project.afterEvaluate(proj -> {

			scanDepsPost(proj).forEach(p -> {
				// Now you can use 'dependentProject' as needed
				String dependentProjectPath = p.getPath();
				project.getLogger().lifecycle("Current project (" + project.getName() + ") depends on: " + dependentProjectPath);

				Object ext = p.getExtensions().findByName("xomda");
				if (null != ext) {
					XOmdaGradlePluginExtension xomdaExt = new XOmdaGradlePluginUndecoratedExtension(ext);
					project.getLogger().lifecycle(" > models: " + xomdaExt.getModels().get());
				}
			});
		});
	}
}
