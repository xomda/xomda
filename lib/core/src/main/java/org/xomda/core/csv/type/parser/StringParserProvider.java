package org.xomda.core.csv.type.parser;

public class StringParserProvider extends org.xomda.core.csv.type.parser.AbstractValueParserProvider {

    public StringParserProvider() {
        super(createPredicate(String.class), asPrimitiveParser(s -> s, null));
    }

}
