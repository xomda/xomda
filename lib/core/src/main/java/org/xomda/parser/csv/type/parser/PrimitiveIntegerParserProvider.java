package org.xomda.parser.csv.type.parser;

public class PrimitiveIntegerParserProvider extends AbstractValueParserProvider {

	public PrimitiveIntegerParserProvider() {
		super(createPredicate(int.class), asPrimitiveParser(Integer::parseInt, 0));
	}

}
