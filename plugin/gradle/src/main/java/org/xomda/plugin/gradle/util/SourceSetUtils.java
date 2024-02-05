package org.xomda.plugin.gradle.util;

import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.xomda.plugin.gradle.Constants;

public class SourceSetUtils {

	public static final String OMDA_SOURCESET_NAME = Constants.XOMDA_CONFIGURATION;

	public static SourceSetContainer getSourceSetContainer(Project project) {
		return project.getExtensions().getByType(SourceSetContainer.class);
	}

	public static SourceSet createOmdaSourceSet(Project project) {
		return createOmdaSourceSet(getSourceSetContainer(project));
	}

	public static SourceSet createOmdaSourceSet(SourceSetContainer sourceSets) {
		SourceSet omdaSourceSet = sourceSets.create(OMDA_SOURCESET_NAME);

		// Configure the source set's directories
		omdaSourceSet.getResources().srcDirs(Constants.XOMDA_CSV_CONFIG_PATH);
		omdaSourceSet.getResources().getFilter().include("*.csv");

		omdaSourceSet.getJava().srcDirs(Constants.XOMDA_JAVA_PATH);
		omdaSourceSet.getJava().srcDirs(Constants.XOMDA_GENERATED_PATH);

		return omdaSourceSet;
	}

	public static SourceSet getOmdaSourceSet(Project project) {
		return getSourceSetContainer(project).findByName(OMDA_SOURCESET_NAME);
	}

}
