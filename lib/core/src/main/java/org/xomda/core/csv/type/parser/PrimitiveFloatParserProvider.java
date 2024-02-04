package org.xomda.core.csv.type.parser;

public class PrimitiveFloatParserProvider extends AbstractValueParserProvider {

    public PrimitiveFloatParserProvider() {
        super(createPredicate(float.class), asPrimitiveParser(Float::parseFloat, 0f));
    }

}
