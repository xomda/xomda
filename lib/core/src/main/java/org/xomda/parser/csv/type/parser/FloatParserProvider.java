package org.xomda.parser.csv.type.parser;

public class FloatParserProvider extends AbstractValueParserProvider.Nullable {

	public FloatParserProvider() {
		super(createPredicate(Float.class), asPrimitiveParser(Float::parseFloat));
	}

}
