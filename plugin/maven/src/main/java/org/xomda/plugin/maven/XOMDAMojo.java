package org.xomda.plugin.maven;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

@Mojo(
		name = "XOMDA-plugin",
		defaultPhase = LifecyclePhase.COMPILE,
		requiresDependencyResolution = ResolutionScope.COMPILE
)
public class XOMDAMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	private MavenProject project;

	@Parameter(property = "classpath", defaultValue = "")
	private Set<String> classpath = new HashSet<>();

	@Parameter(property = "models")
	private Set<String> models = new HashSet<>();

	@Parameter(property = "plugins")
	private Set<String> plugins = new HashSet<>();

	public void execute() throws MojoExecutionException, MojoFailureException {
		@SuppressWarnings("unchecked")
		List<Dependency> dependencies = project.getDependencies();

	}

}