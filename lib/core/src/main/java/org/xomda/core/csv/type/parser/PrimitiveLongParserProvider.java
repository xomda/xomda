package org.xomda.core.csv.type.parser;

public class PrimitiveLongParserProvider extends org.xomda.core.csv.type.parser.AbstractValueParserProvider {

    public PrimitiveLongParserProvider() {
        super(createPredicate(long.class), asPrimitiveParser(Long::parseLong, 0L));
    }

}
