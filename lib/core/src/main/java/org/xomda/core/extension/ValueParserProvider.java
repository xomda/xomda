package org.xomda.core.extension;

import java.util.function.Predicate;

import org.xomda.parser.csv.type.CsvValueParserFunction;

public interface ValueParserProvider extends XOMDAExtension, Predicate<Class<?>>, CsvValueParserFunction {
}
