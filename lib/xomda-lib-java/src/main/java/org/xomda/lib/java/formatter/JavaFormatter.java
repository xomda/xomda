package org.xomda.lib.java.formatter;

import java.io.IOException;

public interface JavaFormatter {

	default <T> void startObject(T obj) throws IOException {
	}

	default <T> void openObject(T obj) throws IOException {
	}

	default <T> void closeObject(T obj) throws IOException {
	}

	default <T> void endObject(T clazz) throws IOException {
	}

}
