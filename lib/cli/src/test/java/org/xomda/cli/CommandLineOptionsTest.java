package org.xomda.cli;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;
import org.xomda.core.config.Configuration;
import org.xomda.core.module.XOMDAReverseEntity;
import org.xomda.core.module.XOMDATypeRefs;
import org.xomda.core.module.template.XOMDACodeTemplate;

public class CommandLineOptionsTest {

	@Test
	public void testCommandLineOptions() throws ParseException {
		final String extensions = Stream.of(XOMDAReverseEntity.class, XOMDATypeRefs.class, XOMDACodeTemplate.class)
				.map(Class::getName).collect(Collectors.joining(","));

		final String out = "../../xomda";

		final Configuration config = CommandLineOptions.parse("--out", out, "--extensions", extensions);

		assertNotNull(config);

	}

}
