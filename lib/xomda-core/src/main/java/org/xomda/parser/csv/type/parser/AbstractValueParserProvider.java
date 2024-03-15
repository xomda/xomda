package org.xomda.parser.csv.type.parser;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.xomda.core.extension.ValueParserProvider;
import org.xomda.parser.csv.type.ValueParser;
import org.xomda.shared.util.Predicates;

public abstract class AbstractValueParserProvider implements ValueParserProvider {

	private final Predicate<Class<?>> test;
	private final Function<Class<?>, ValueParser> parserFunction;

	protected AbstractValueParserProvider(final Predicate<Class<?>> test, final ValueParser parser) {
		this(test, (Function<Class<?>, ValueParser>) o -> parser);
	}

	AbstractValueParserProvider(final Predicate<Class<?>> test, final Function<Class<?>, ValueParser> parserFunction) {
		this.test = test;
		this.parserFunction = parserFunction;
	}

	@Override
	public boolean test(final Class<?> c) {
		return null != test && test.test(c);
	}

	@Override
	public ValueParser apply(final Class<?> c) {
		return parserFunction.apply(c);
	}

	@SuppressWarnings("unchecked")
	static Predicate<Class<?>> createPredicate(final Class<?>... classes) {
		return Stream.of(classes)
				.map(Predicate::isEqual)
				.reduce(Predicate::or)
				.map(Predicate.class::cast)
				.orElseGet(Predicates::alwaysFalse);
	}

	static ValueParser.Primitive asPrimitiveParser(final ValueParser.Primitive p) {
		return p;
	}

	static ValueParser.Primitive asPrimitiveParser(final ValueParser.Primitive p, final Object defaultValue) {
		return (final String s) -> null == s || s.isBlank()
				? defaultValue
				: p.apply(s);
	}

	static abstract class Nullable extends AbstractValueParserProvider {

		Nullable(final Predicate<Class<?>> test, final ValueParser parser) {
			super(test, parser);
		}

		@Override
		public ValueParser apply(final Class<?> c) {
			final ValueParser parser = super.apply(c);
			return (final String s) -> null == s || s.isEmpty() ? null : parser.apply(s);
		}

	}

}
