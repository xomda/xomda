package org.xomda.core.csv.type.parser;

public class FloatParserProvider extends org.xomda.core.csv.type.parser.AbstractValueParserProvider.Nullable {

    public FloatParserProvider() {
        super(createPredicate(Float.class), asPrimitiveParser(Float::parseFloat));
    }

}
