package org.xomda.parser.csv.type;

import java.util.function.Function;

public interface CsvValueParserFunction extends Function<Class<?>, ValueParser> {
}
