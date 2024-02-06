package org.xomda.core;

import java.io.IOException;
import java.util.List;

import org.xomda.core.config.Configuration;
import org.xomda.core.csv.CsvService;

public class XOMDA {
	public static <T> List<T> read(String filename, Configuration config) throws IOException {
		return new CsvService().read(filename, config);
	}

	public static void initExtensions(Configuration config) {

	}

}
