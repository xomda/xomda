package org.xomda.core.csv.type.parser;

public class IntegerParserProvider extends org.xomda.core.csv.type.parser.AbstractValueParserProvider.Nullable {

    public IntegerParserProvider() {
        super(createPredicate(Integer.class), asPrimitiveParser(Integer::parseInt));
    }

}
