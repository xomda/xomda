package org.xomda.core.csv.type.parser;

public class DoubleParserProvider extends org.xomda.core.csv.type.parser.AbstractValueParserProvider.Nullable {

    public DoubleParserProvider() {
        super(createPredicate(Double.class), asPrimitiveParser(Double::parseDouble));
    }

}
