package org.xomda.plugin.gradle.util;

import static org.xomda.plugin.gradle.Constants.XOMDA_GENERATED_SOURCE_NAME;
import static org.xomda.plugin.gradle.Constants.XOMDA_SOURCESET_NAME;

import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.xomda.plugin.gradle.Constants;

public class SourceSetUtils {

	public static void addGeneratedSources(final Project project) {
		final SourceSet mainSourceSet = getSourceSetContainer(project).getByName("main");
		mainSourceSet.getJava()
				.srcDirs("src/" + XOMDA_GENERATED_SOURCE_NAME + "/java");
	}

	public static SourceSetContainer getSourceSetContainer(final Project project) {
		return project.getExtensions().getByType(SourceSetContainer.class);
	}

	public static SourceSet createOmdaSourceSet(final Project project) {
		return createOmdaSourceSet(getSourceSetContainer(project));
	}

	public static SourceSet createOmdaSourceSet(final SourceSetContainer sourceSets) {
		final SourceSet omdaSourceSet = sourceSets.create(XOMDA_SOURCESET_NAME);

		// Configure the source set's directories
		omdaSourceSet.getResources().srcDirs(Constants.XOMDA_CSV_CONFIG_PATH);
		omdaSourceSet.getResources().getFilter().include("*.csv");

		omdaSourceSet.getJava().srcDirs(Constants.XOMDA_JAVA_PATH);
		omdaSourceSet.getJava().srcDirs(Constants.XOMDA_GENERATED_PATH);

		return omdaSourceSet;
	}

	public static SourceSet getOmdaSourceSet(final Project project) {
		return getSourceSetContainer(project).findByName(XOMDA_SOURCESET_NAME);
	}

}
