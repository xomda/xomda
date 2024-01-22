package org.xomda.core.util;

import java.util.stream.Stream;

import org.xomda.core.csv.CsvObject;
import org.xomda.core.csv.CsvSchema;
import org.xomda.core.extension.CsvObjectProcessor;
import org.xomda.core.extension.CsvSchemaProcessor;
import org.xomda.core.extension.XOmdaExtension;
import org.xomda.core.extension.ValueParserProvider;

public class Extensions {

    public static void init(org.xomda.core.util.ParseContext context) {
        getExtensions(context).forEach((XOmdaExtension extension) -> extension.init(context));
    }

    public static void process(org.xomda.core.util.ParseContext context, CsvSchema schema) {
        getSchemaProcessors(context).forEach((CsvSchemaProcessor extension) -> extension.process(schema));
    }

    public static void process(org.xomda.core.util.ParseContext context, CsvObject csvObject) {
        getObjectProcessors(context).forEach((CsvObjectProcessor extension) -> extension.process(csvObject));
    }

    public static Stream<CsvSchemaProcessor> getSchemaProcessors(org.xomda.core.util.ParseContext context) {
        return getExtensions(context, CsvSchemaProcessor.class);
    }

    public static Stream<CsvObjectProcessor> getObjectProcessors(org.xomda.core.util.ParseContext context) {
        return getExtensions(context, CsvObjectProcessor.class);
    }

    public static Stream<ValueParserProvider> getValueParserProviders(org.xomda.core.util.ParseContext context) {
        return getExtensions(context, ValueParserProvider.class);
    }

    static <T> Stream<T> getExtensions(org.xomda.core.util.ParseContext context, Class<T> type) {
        return getExtensions(context).filter(type::isInstance).map(type::cast);
    }

    static Stream<? extends XOmdaExtension> getExtensions(org.xomda.core.util.ParseContext context) {
        return context.getExtensions().stream();
    }

}