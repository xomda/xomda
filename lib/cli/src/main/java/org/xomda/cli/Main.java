package org.xomda.cli;

import static org.xomda.shared.exception.SneakyThrow.sneaky;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

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
		Stream.of(config.getModels()).forEach(sneaky((String csvFilename) -> {
			// 1) parse
			List<?> result = new CsvService().read(csvFilename, config);
			// 2) fetch
			final Object pkg = result.isEmpty() ? null : result.get(0);
			if (null == pkg) {
				return;
			}
			// 3) generate
			config.getTemplates().forEach(sneaky(t -> {
				TemplateContext templateContext = new TemplateContext(config.getOutDir());
				t.generate(pkg, templateContext);
			}));
		}));
	}

}
