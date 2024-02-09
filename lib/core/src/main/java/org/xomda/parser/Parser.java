package org.xomda.parser;

import java.io.IOException;
import java.util.List;

import org.xomda.core.config.Configuration;
import org.xomda.core.extension.Loggable;

@FunctionalInterface
public interface Parser extends Loggable {

	default <T> List<T> parse(final String filename, final Configuration config) throws IOException {
		return parse(new String[] { filename }, config);
	}

	<T> List<T> parse(final String[] filenames, final Configuration config) throws IOException;

}
