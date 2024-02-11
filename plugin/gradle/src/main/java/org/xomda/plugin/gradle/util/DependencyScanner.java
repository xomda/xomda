package org.xomda.plugin.gradle.util;

import java.util.stream.Stream;

import org.gradle.api.Project;
import org.gradle.api.artifacts.ProjectDependency;
import org.xomda.plugin.gradle.XOmdaGradlePluginExtension;

public class DependencyScanner {

	/**
	 * Returns a stream of distinct models definitions (CSV) upon which this project relies
	 */
	public static Stream<String> dependentModels(Project project) {
		return scanDepsPost(project)
				.flatMap(p -> {
					Object ext = p.getExtensions().findByName("xomda");
					if (null == ext) {
						return Stream.empty();
					}
					XOmdaGradlePluginExtension xomdaExt = new XOmdaGradlePluginUndecoratedExtension(ext);
					return xomdaExt.getModels().get().stream();
				})
				.distinct();
	}

	/**
	 * Returns an array of distinct models definitions (CSV) upon which this project relies
	 */
	public static String[] getDependentModels(Project project) {
		return dependentModels(project).toArray(String[]::new);
	}

	/**
	 * Returns a stream of projects upon the given project depends (deep)
	 */
	private static Stream<Project> scanDepsPost(Project project) {
		return project.getConfigurations().stream().flatMap(configuration -> configuration.getDependencies()
				.stream()
				.filter(ProjectDependency.class::isInstance)
				.flatMap(dependency -> {
					ProjectDependency projectDependency = (ProjectDependency) dependency;
					Project dependentProject = projectDependency.getDependencyProject();
					return Stream.concat(
							Stream.of(dependentProject),
							scanDepsPost(dependentProject)
					);
				}));
	}
}
