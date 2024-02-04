package org.xomda.core.csv.type.parser;

public class PrimitiveIntegerParserProvider extends AbstractValueParserProvider {

    public PrimitiveIntegerParserProvider() {
        super(createPredicate(int.class), asPrimitiveParser(Integer::parseInt, 0));
    }

}
