package org.xomda.core.csv.type.parser;

public class DoubleParserProvider extends AbstractValueParserProvider.Nullable {

	public DoubleParserProvider() {
		super(createPredicate(Double.class), asPrimitiveParser(Double::parseDouble));
	}

}
