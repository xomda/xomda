package org.xomda.plugin.gradle.util;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.gradle.api.Project;
import org.gradle.api.artifacts.ProjectDependency;
import org.xomda.plugin.gradle.XOMDAGradlePluginExtension;

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
					XOMDAGradlePluginExtension xomdaExt = new XOMDAGradlePluginUndecoratedExtension(ext);
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
		return scanDepsPost(project, new HashSet<>());
	}

	private static Stream<Project> scanDepsPost(Project project, Set<Project> cache) {
		if (cache.contains(project)) {
			return Stream.empty();
		}
		cache.add(project);
		return project.getConfigurations().stream().flatMap(
						configuration -> configuration.getDependencies().stream()
								// filter out dependencies on other projects
								.filter(ProjectDependency.class::isInstance)
								.map(ProjectDependency.class::cast)
								.map(ProjectDependency::getDependencyProject)

								// return a stream of the current project and its dependent projects
								.flatMap(dependentProject -> Stream.concat(
										Stream.of(dependentProject),
										scanDepsPost(dependentProject, cache)
								))
				)
				.distinct(); // still necessary with the Set?
	}
}
