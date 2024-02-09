package org.xomda.plugin.gradle;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * A simple functional test for the XOMDA Gradle plugin.
 */
class XOmdaGradlePluginFunctionalTest {

	@TempDir
	File projectDir;

	private File getBuildFile() {
		return new File(projectDir, "build.gradle");
	}

	private File getSettingsFile() {
		return new File(projectDir, "settings.gradle");
	}

	@Test
	void canRunTask() throws IOException {
		writeString(getSettingsFile(), "");
		writeString(getBuildFile(), "plugins {" + "  id('org.xomda.plugin-gradle')" + "}");

		// Run the build
		final GradleRunner runner = GradleRunner.create();
		runner.forwardOutput();
		runner.withPluginClasspath();
		runner.withArguments("xomdaGenerate");
		runner.withProjectDir(projectDir);
		final BuildResult result = runner.build();

		// Verify the result
		assertTrue(result.getOutput().contains(" "));
	}

	private void writeString(final File file, final String string) throws IOException {
		try (Writer writer = new FileWriter(file)) {
			writer.write(string);
		}
	}
}
