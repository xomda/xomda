package org.xomda.core.csv.type.parser;

public class PrimitiveLongParserProvider extends AbstractValueParserProvider {

    public PrimitiveLongParserProvider() {
        super(createPredicate(long.class), asPrimitiveParser(Long::parseLong, 0L));
    }

}
