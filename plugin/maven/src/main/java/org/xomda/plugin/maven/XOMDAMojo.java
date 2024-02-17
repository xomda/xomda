package org.xomda.plugin.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "XOMDA-plugin", defaultPhase = LifecyclePhase.COMPILE)
public class XOMDAMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	MavenProject project;

	@Parameter(property = "scope")
	String scope;

	public void execute() throws MojoExecutionException, MojoFailureException {

	}

}