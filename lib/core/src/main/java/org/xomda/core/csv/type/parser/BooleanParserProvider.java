package org.xomda.core.csv.type.parser;

public class BooleanParserProvider extends org.xomda.core.csv.type.parser.AbstractValueParserProvider.Nullable {

    public BooleanParserProvider() {
        super(createPredicate(Boolean.class), asPrimitiveParser(Boolean::parseBoolean));
    }

}
