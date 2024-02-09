package org.xomda.plugin.gradle;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

/**
 * A simple unit test for the 'xomda' plugin.
 */
class XOmdaGradlePluginTest {
	@Test
	void pluginRegistersATask() {
		// Create a test project and apply the plugin
		final Project project = ProjectBuilder.builder().build();
		project.getPlugins().apply("org.xomda.plugin-gradle");

		// Verify the result
		assertNotNull(project.getTasks().findByName("xomdaGenerate"));
	}
}
