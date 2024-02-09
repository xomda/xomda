package org.xomda.core;

import java.io.IOException;
import java.util.List;

import org.xomda.core.config.Configuration;
import org.xomda.parser.csv.CsvService;

public class XOMDA {

	public static <T> List<T> read(final String filename, final Configuration config) throws IOException {
		return new CsvService().read(filename, config);
	}

	public static <T> List<T> read(final String[] filenames, final Configuration config) throws IOException {
		return new CsvService().read(filenames, config);
	}

	// TODO: process the parsed objects (send to template service)
	public static void process() {

	}

	public static void initExtensions(final Configuration config) {
	}

}
