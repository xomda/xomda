package org.xomda.core.csv.type.parser;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.xomda.core.csv.type.ValueParser;
import org.xomda.core.extension.ValueParserProvider;
import org.xomda.shared.util.Predicates;

public abstract class AbstractValueParserProvider implements ValueParserProvider {

	private final Predicate<Class<?>> test;
	private final Function<Class<?>, ValueParser> parserFunction;

	protected AbstractValueParserProvider(Predicate<Class<?>> test, ValueParser parser) {
		this(test, (Function<Class<?>, ValueParser>) o -> parser);
	}

	AbstractValueParserProvider(Predicate<Class<?>> test, Function<Class<?>, ValueParser> parserFunction) {
		this.test = test;
		this.parserFunction = parserFunction;
	}

	@Override
	public boolean test(Class<?> c) {
		return null != test && test.test(c);
	}

	@Override
	public ValueParser apply(Class<?> c) {
		return parserFunction.apply(c);
	}

	@SuppressWarnings("unchecked")
	static Predicate<Class<?>> createPredicate(Class<?>... classes) {
		return (Predicate<Class<?>>) Stream.of(classes).map(Predicate::isEqual).reduce(Predicate::or)
				.map(Predicate.class::cast).orElseGet(Predicates::alwaysFalse);
	}

	static ValueParser asParser(ValueParser p) {
		return p;
	}

	static ValueParser asParser(ValueParser p, Object defaultValue) {
		return (String s) -> null == s || s.isBlank() ? defaultValue : p.apply(s);
	}

	static ValueParser.Primitive asPrimitiveParser(ValueParser.Primitive p) {
		return p;
	}

	static ValueParser.Primitive asPrimitiveParser(ValueParser.Primitive p, Object defaultValue) {
		return (String s) -> null == s || s.isBlank() ? defaultValue : p.apply(s);
	}

	static abstract class Nullable extends AbstractValueParserProvider {

		Nullable(Predicate<Class<?>> test, ValueParser parser) {
			super(test, parser);
		}

		@Override
		public ValueParser apply(Class<?> c) {
			ValueParser parser = super.apply(c);
			return (String s) -> null == s || s.isEmpty() ? null : parser.apply(s);
		}

	}

}
