package org.xomda.cli;

import static org.xomda.shared.exception.SneakyThrow.sneaky;

import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.ParseException;
import org.xomda.core.config.Configuration;
import org.xomda.parser.csv.CsvService;
import org.xomda.shared.logging.LogService;
import org.xomda.template.TemplateContext;

/**
 * The main command-line entry point for XOMDA.
 * <p>
 * Run it with "<code>--help</code>" to see the available command-line options.
 */
public class Main {

	public static void main(final String[] args) throws IOException, ParseException {
		// parse the config (from the command-line)
		Configuration config = CommandLineOptions
				.tryBuild(args)
				.withDefaultOmdaExtensions()
				.build();
		// set the log level
		LogService.setDefaultLogLevel(config.getLogLevel());

		// parse each model
		List<?> result = new CsvService().read(config.getModels(), config);

		if (result.isEmpty()) {
			return;
		}

		// 3) generate
		config.getTemplates().forEach(sneaky(t -> {
			TemplateContext templateContext = new TemplateContext(config.getOutDir(), result);
			t.generate(result.get(0), templateContext);
		}));
	}

}
