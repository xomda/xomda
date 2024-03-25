package org.xomda.lib.java.formatter;

import java.io.IOException;

public interface JavaFormatter {

	default <T> void startObject(T obj) throws IOException {
	}

	default void startGroup(char obj) throws IOException {
		startGroup("" + obj);
	}

	default void startGroup(String obj) throws IOException {
	}

	default void endGroup(char obj) throws IOException {
		endGroup("" + obj);
	}

	default void endGroup(String obj) throws IOException {
	}

	default <T> void endObject(T clazz) throws IOException {
	}

}
