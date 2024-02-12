package org.xomda.core.util;

import java.util.stream.Stream;

import org.xomda.core.config.Configuration;
import org.xomda.core.extension.CsvObjectProcessor;
import org.xomda.core.extension.CsvSchemaProcessor;
import org.xomda.core.extension.ValueParserProvider;
import org.xomda.core.extension.XOMDAExtension;
import org.xomda.parser.ParseContext;
import org.xomda.parser.csv.CsvObject;
import org.xomda.parser.csv.CsvSchema;
import org.xomda.template.Template;

public class Extensions {

	public static void init(final ParseContext context) {
		getExtensions(context.getConfig()).forEach((final XOMDAExtension extension) -> extension.init(context));
	}

	public static void process(final Configuration configuration, final CsvSchema schema) {
		getSchemaProcessors(configuration).forEach((final CsvSchemaProcessor extension) -> extension.process(schema));
	}

	public static void process(final Configuration configuration, final CsvObject csvObject) {
		getObjectProcessors(configuration).forEach((final CsvObjectProcessor extension) -> extension.process(csvObject));
	}

	public static Stream<CsvSchemaProcessor> getSchemaProcessors(final Configuration configuration) {
		return getExtensions(configuration, CsvSchemaProcessor.class);
	}

	public static Stream<CsvObjectProcessor> getObjectProcessors(final Configuration configuration) {
		return getExtensions(configuration, CsvObjectProcessor.class);
	}

	public static Stream<ValueParserProvider> getValueParserProviders(final Configuration configuration) {
		return getExtensions(configuration, ValueParserProvider.class);
	}

	@SuppressWarnings("unchecked")
	public static <T> Stream<Template<T>> getTemplates(final Configuration configuration) {
		return getExtensions(configuration, Template.class).map(t -> (Template<T>) t);
	}

	static <T> Stream<T> getExtensions(final Configuration configuration, final Class<T> type) {
		return getExtensions(configuration).filter(type::isInstance).map(type::cast);
	}

	static Stream<? extends XOMDAExtension> getExtensions(final Configuration configuration) {
		return configuration.getPlugins().stream();
	}

}