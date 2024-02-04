package org.xomda.cli;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;
import org.xomda.core.config.Configuration;
import org.xomda.core.module.XOmdaReverseEntity;
import org.xomda.core.module.XOmdaTypeRefs;
import org.xomda.core.module.template.XOmdaCodeTemplate;

public class CliOptionsTest {

    @Test
    public void test() throws ParseException {
        final String extensions = Stream
            .of(XOmdaReverseEntity.class, XOmdaTypeRefs.class, XOmdaCodeTemplate.class)
            .map(Class::getName)
            .collect(Collectors.joining(","));

        final String out = "../../xomda";

        final Configuration config = CommandLineOptions.parse(
            "--out", out,
            "--extensions", extensions);

        assertNotNull(config);

    }

}
