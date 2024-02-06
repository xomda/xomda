package org.xomda.plugin.gradle.util;

import static org.xomda.plugin.gradle.Constants.XOMDA_CONFIGURATION;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;
import java.util.stream.Stream;

import org.gradle.api.Project;
import org.gradle.api.tasks.compile.JavaCompile;
import org.xomda.plugin.gradle.task.XOmdaCompileTemplatesTask;

public class XOMDATemplateClassLoader extends URLClassLoader {
	public XOMDATemplateClassLoader(JavaCompile task) {
		this(task, XOmdaCompileTemplatesTask.class.getClassLoader());
	}

	public XOMDATemplateClassLoader(JavaCompile task, ClassLoader parent) {
		super(getUrls(task), parent);
	}

	static URL[] getUrls(JavaCompile task) {
		Project project = task.getProject();
		URL[] urls = null;
		try {
			urls = Stream
					.concat(
							Stream.of(task.getDestinationDirectory().get().getAsFile().toURI().toURL()),
							project
									.files(project.getConfigurations().getAt(XOMDA_CONFIGURATION))
									.getFiles().stream().map(f -> {
										try {
											return f.toURI().toURL();
										} catch (MalformedURLException e) {
											project.getLogger().error("", e);
											return null;
										}
									})
									.filter(Objects::nonNull)
					)
					.toArray(URL[]::new);
		} catch (MalformedURLException e) {
			project.getLogger().error("", e);
			urls = new URL[0];
		}
		return urls;
	}

}
