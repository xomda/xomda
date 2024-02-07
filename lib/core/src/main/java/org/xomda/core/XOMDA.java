package org.xomda.core;

import java.io.IOException;
import java.util.List;

import org.xomda.core.config.Configuration;
import org.xomda.parser.csv.CsvService;

public class XOMDA {

	public static <T> List<T> read(final String filename, final Configuration config) throws IOException {

		return new CsvService().read(filename, config);
	}

	public static void initExtensions(final Configuration config) {
	}

}
