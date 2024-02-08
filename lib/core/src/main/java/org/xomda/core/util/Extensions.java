package org.xomda.core.util;

import java.util.stream.Stream;

import org.xomda.core.extension.CsvObjectProcessor;
import org.xomda.core.extension.CsvSchemaProcessor;
import org.xomda.core.extension.ValueParserProvider;
import org.xomda.core.extension.XOmdaExtension;
import org.xomda.parser.ParseContext;
import org.xomda.parser.csv.CsvObject;
import org.xomda.parser.csv.CsvSchema;

public class Extensions {

	public static void init(final ParseContext context) {
		getExtensions(context).forEach((final XOmdaExtension extension) -> extension.init(context));
	}

	public static void process(final ParseContext context, final CsvSchema schema) {
		getSchemaProcessors(context).forEach((final CsvSchemaProcessor extension) -> extension.process(schema));
	}

	public static void process(final ParseContext context, final CsvObject csvObject) {
		getObjectProcessors(context).forEach((final CsvObjectProcessor extension) -> extension.process(csvObject));
	}

	public static Stream<CsvSchemaProcessor> getSchemaProcessors(final ParseContext context) {
		return getExtensions(context, CsvSchemaProcessor.class);
	}

	public static Stream<CsvObjectProcessor> getObjectProcessors(final ParseContext context) {
		return getExtensions(context, CsvObjectProcessor.class);
	}

	public static Stream<ValueParserProvider> getValueParserProviders(final ParseContext context) {
		return getExtensions(context, ValueParserProvider.class);
	}

	static <T> Stream<T> getExtensions(final ParseContext context, final Class<T> type) {
		return getExtensions(context).filter(type::isInstance).map(type::cast);
	}

	static Stream<? extends XOmdaExtension> getExtensions(final ParseContext context) {
		return context.getExtensions().stream();
	}

}