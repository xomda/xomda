package org.xomda.core.csv.type.parser;

public class PrimitiveDoubleParserProvider extends org.xomda.core.csv.type.parser.AbstractValueParserProvider {

    public PrimitiveDoubleParserProvider() {
        super(createPredicate(double.class), asPrimitiveParser(Double::parseDouble, 0d));
    }

}
