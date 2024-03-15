package org.xomda.parser.csv.type;

import java.util.function.Function;

@FunctionalInterface
public interface ValueParser extends Function<String, Object> {

	/**
	 * Will immediately assign the value.
	 */
	@FunctionalInterface
	interface Primitive extends ValueParser {
	}

	/**
	 * (Default) Will only assign the value after the model is parsed.
	 */
	@FunctionalInterface
	interface Deferred extends ValueParser {
	}

	@FunctionalInterface
	interface Optional extends ValueParser.Deferred {
	}

}
