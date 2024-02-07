package org.xomda.parser.csv.type.parser;

public class PrimitiveFloatParserProvider extends AbstractValueParserProvider {

	public PrimitiveFloatParserProvider() {
		super(createPredicate(float.class), asPrimitiveParser(Float::parseFloat, 0f));
	}

}
