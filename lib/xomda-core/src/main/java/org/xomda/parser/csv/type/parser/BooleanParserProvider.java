package org.xomda.parser.csv.type.parser;

public class BooleanParserProvider extends AbstractValueParserProvider.Nullable {

	public BooleanParserProvider() {
		super(createPredicate(Boolean.class), asPrimitiveParser(Boolean::parseBoolean));
	}

}
