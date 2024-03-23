package org.xomda.shared.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

public class PackageScanner {

	private static final Pattern RX_DOT = Pattern.compile("[.]");

	private static String toPath(String name) {
		return RX_DOT.matcher(name).replaceAll("/");
	}

	public void test() {
		ClassLoader cl = getClass().getClassLoader();
		Package[] pkg = cl.getDefinedPackages();

		Arrays.stream(pkg)
				.map(Package::getName)
				.forEach(System.out::println);

		String path = toPath(getClass().getPackageName());
		try (final InputStream stream = cl.getResourceAsStream(path)) {
			if (null == stream) {
				return;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			reader.lines()
					.filter(line -> line.endsWith(".class"))
					.map(line -> getClass(line, getClass().getPackageName()))
					.filter(Objects::nonNull)
					.map(Class::getName)
					.forEach(System.out::println);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Class<?> getClass(String className, String packageName) {
		try {
			return Class.forName(packageName + "."
					+ className.substring(0, className.lastIndexOf('.')));
		} catch (ClassNotFoundException e) {
			// handle the exception
		}
		return null;
	}

}
