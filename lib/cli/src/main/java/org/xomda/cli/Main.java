package org.xomda.cli;

import static org.xomda.shared.exception.SneakyThrow.sneaky;

import java.io.IOException;
import java.util.List;

import org.xomda.core.XOMDA;
import org.xomda.core.config.Configuration;
import org.xomda.core.util.Extensions;
import org.xomda.shared.logging.LogService;
import org.xomda.template.TemplateContext;

/**
 * The main command-line entry point for XOMDA.
 * <p>
 * Run it with "<code>--help</code>" to see the available command-line options.
 */
public class Main {

	public static void main(final String[] args) throws IOException {
		// parse the config (from the command-line)
		Configuration config = CommandLineOptions
				.tryBuild(args)
				.withDefaultXOMDAExtensions()
				.build();

		// set the log level
		LogService.setDefaultLogLevel(config.getLogLevel());

		// parse each model
		List<?> result = XOMDA.parse(config.getModels(), config);

		if (result.isEmpty()) {
			return;
		}

		Object first = result.get(0);

		// process the templates
		Extensions.getTemplates(config).forEach(sneaky(t -> {
			TemplateContext templateContext = new TemplateContext(config.getOutDir(), result);
			t.generate(first, templateContext);
		}));
	}

}
