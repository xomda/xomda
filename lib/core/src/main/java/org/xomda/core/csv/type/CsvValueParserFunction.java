package org.xomda.core.csv.type;

import java.util.function.Function;

public interface CsvValueParserFunction extends Function<Class<?>, ValueParser> {
}
