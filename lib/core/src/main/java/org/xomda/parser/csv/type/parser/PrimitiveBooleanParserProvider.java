package org.xomda.parser.csv.type.parser;

public class PrimitiveBooleanParserProvider extends AbstractValueParserProvider {

	public PrimitiveBooleanParserProvider() {
		super(createPredicate(boolean.class), asPrimitiveParser(Boolean::parseBoolean));
	}

}
