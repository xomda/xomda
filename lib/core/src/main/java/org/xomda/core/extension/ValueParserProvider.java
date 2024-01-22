package org.xomda.core.extension;

import java.util.function.Predicate;

import org.xomda.core.csv.type.CsvValueParserFunction;

public interface ValueParserProvider extends XOmdaExtension, Predicate<Class<?>>, CsvValueParserFunction {

}
