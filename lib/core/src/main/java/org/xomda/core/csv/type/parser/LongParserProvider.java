package org.xomda.core.csv.type.parser;

public class LongParserProvider extends AbstractValueParserProvider.Nullable {

	public LongParserProvider() {
		super(createPredicate(Long.class), asPrimitiveParser(Long::parseLong));
	}

}
