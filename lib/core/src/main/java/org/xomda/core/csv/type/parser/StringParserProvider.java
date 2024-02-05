package org.xomda.core.csv.type.parser;

public class StringParserProvider extends AbstractValueParserProvider {

	public StringParserProvider() {
		super(createPredicate(String.class), asPrimitiveParser(s -> s, null));
	}

}
