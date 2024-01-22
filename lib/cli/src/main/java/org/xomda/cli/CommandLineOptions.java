package org.xomda.cli;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.Level;
import org.xomda.core.Constants;
import org.xomda.core.config.Configuration;
import org.xomda.core.config.ConfigurationBuilder;
import org.xomda.shared.exception.SneakyThrow;

@SuppressWarnings("serial")
public class CommandLineOptions extends Options {

    private static final String COMMAND = "xomda";

    private final static Option OPT_EXTENSION = Option.builder("e")
        .longOpt("extensions")
        .hasArg(true)
        .argName("EXTENSIONS")
        .desc("Specify a comma-separated list of extensions.")
        .build();

    private final static Option OPT_MODELS = Option.builder("m")
        .longOpt("models")
        .hasArg(true)
        .argName("MODELS")
        .desc("Specify one or more model files, separated by comma.")
        .build();

    private final static Option OPT_OUT_DIR = Option.builder("o")
        .longOpt("out")
        .hasArg(true)
        .argName("DIR")
        .desc("Specify the output directory.")
        .build();

    private final static Option OPT_CLASSPATHS = Option.builder("c")
        .longOpt("classpath")
        .hasArg(true)
        .argName("CLASSPATH")
        .desc("Specify one or more packages to scan for interfaces, separated by comma.")
        .build();

    private final static Option OPT_LOG_LEVEL = Option.builder("l")
        .longOpt("level")
        .hasArg(true)
        .argName("LOG-LEVEL")
        .desc("Specify the log-level.")
        .build();

    private final static Option OPT_HELP = Option.builder("h")
        .longOpt("help")
        .desc("Show this help")
        .build();

    public CommandLineOptions() {
        addOption(OPT_EXTENSION);
        addOption(OPT_MODELS);
        addOption(OPT_OUT_DIR);
        addOption(OPT_CLASSPATHS);
        addOption(OPT_LOG_LEVEL);
        addOption(OPT_HELP);
    }

    public static Configuration parse(final String... args) throws ParseException {
        return parse(Configuration.builder(), args).build();
    }

    public static ConfigurationBuilder tryBuild(final String... args) {
        return tryParse(Configuration.builder(), args);
    }

    public static ConfigurationBuilder tryParse(final ConfigurationBuilder builder, final String... args) {
        try {
            return parse(builder, args);
        } catch (final ParseException e) {
            CommandLineOptions.printUsage();
            System.exit(0);
        }
        return null;
    }

    public static ConfigurationBuilder parse(final ConfigurationBuilder builder, final String... args) throws ParseException {
        final CommandLineParser parser = new DefaultParser();
        final CommandLine line = parser.parse(new CommandLineOptions(), args);

        if (line.hasOption(OPT_HELP)) {
            printHelp();
            System.exit(0);
        }

        if (line.hasOption(OPT_OUT_DIR)) {
            Optional
                .ofNullable(line.getOptionValue(OPT_OUT_DIR))
                .filter(Predicate.not(String::isBlank))
                .map(Paths::get)
                .map(p -> p.isAbsolute() ? p : p.toAbsolutePath())
                .map(Path::toString)
                .ifPresent(builder::withOutDir);
        }

        if (line.hasOption(OPT_CLASSPATHS)) {
            builder.withClassPath(list(line.getOptionValues(OPT_CLASSPATHS)));
        }

        if (line.hasOption(OPT_EXTENSION)) {
            builder.addExtensions(
                stream(line.getOptionValues(OPT_EXTENSION))
                    .toArray(Object[]::new));
        }

        builder.withModels(
            line.hasOption(OPT_MODELS)
                ? stream(line.getOptionValues(OPT_MODELS)).toList()
                : Stream.of(
                    Constants.XOMDA_DOT_PATH,
                    Constants.XOMDA_CSV_CONFIG_PATH)
                .filter(Files::exists)
                .flatMap(SneakyThrow.sneaky(Files::list))
                .filter(Predicate.not(Files::isDirectory))
                .filter(p -> p.getFileName().toString().endsWith(".csv"))
                .map(Path::toAbsolutePath)
                .map(Path::toString)
                .toList());

        if (line.hasOption(OPT_LOG_LEVEL)) {
            Optional
                .ofNullable(line.getOptionValue(OPT_LOG_LEVEL))
                .filter(Predicate.not(String::isBlank))
                .map(Level::getLevel)
                .ifPresent(builder::withLogLevel);
        }

        return builder;
    }

    static Stream<String> stream(final String[] args) {
        return null == args ? Stream.empty()
            : Stream.of(args)
            .map(v -> v.split(","))
            .flatMap(Stream::of)
            .map(String::trim);
    }

    static Collection<String> list(final String[] args) {
        return stream(args).toList();
    }

    public static void printHelp() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(COMMAND, new CommandLineOptions());
    }

    public static void printUsage() {
        final HelpFormatter formatter = new HelpFormatter();
        final PrintWriter writer = new PrintWriter(System.out);
        formatter.printUsage(writer, 80, COMMAND, new CommandLineOptions());
        writer.flush();
    }

}
