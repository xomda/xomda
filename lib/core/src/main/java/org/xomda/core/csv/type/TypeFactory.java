package org.xomda.core.csv.type;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.xomda.shared.registry.Registry;
import org.xomda.core.csv.type.parser.BooleanParserProvider;
import org.xomda.core.csv.type.parser.DateParserProvider;
import org.xomda.core.csv.type.parser.DoubleParserProvider;
import org.xomda.core.csv.type.parser.EnumParserProvider;
import org.xomda.core.csv.type.parser.FloatParserProvider;
import org.xomda.core.csv.type.parser.IntegerParserProvider;
import org.xomda.core.csv.type.parser.LongParserProvider;
import org.xomda.core.csv.type.parser.PrimitiveBooleanParserProvider;
import org.xomda.core.csv.type.parser.PrimitiveDoubleParserProvider;
import org.xomda.core.csv.type.parser.PrimitiveFloatParserProvider;
import org.xomda.core.csv.type.parser.PrimitiveIntegerParserProvider;
import org.xomda.core.csv.type.parser.PrimitiveLongParserProvider;
import org.xomda.core.csv.type.parser.StringParserProvider;
import org.xomda.core.extension.ValueParserProvider;

/**
 * Extensible Parser Factory. Additional Parsers can be registered.
 */
public class TypeFactory {

    private static final Set<ValueParserProvider> converters = Set.of(
        new StringParserProvider(),
        new PrimitiveBooleanParserProvider(),
        new PrimitiveDoubleParserProvider(),
        new PrimitiveFloatParserProvider(),
        new PrimitiveIntegerParserProvider(),
        new PrimitiveLongParserProvider(),
        new BooleanParserProvider(),
        new DateParserProvider(),
        new DoubleParserProvider(),
        new FloatParserProvider(),
        new IntegerParserProvider(),
        new LongParserProvider(),
        new EnumParserProvider()
    );
    private final Registry<ValueParserProvider> registry = new Registry<>(ConcurrentHashMap::newKeySet);

    public TypeFactory register(ValueParserProvider parser) {
        registry.register(parser);
        return this;
    }

    public TypeFactory register(Stream<ValueParserProvider> parsers) {
        parsers.forEach(this::register);
        return this;
    }

    public TypeFactory register(Iterable<ValueParserProvider> parsers) {
        parsers.forEach(this::register);
        return this;
    }

    public org.xomda.core.csv.type.ValueParser getSetter(final Class<?> type) {
        if (null == type) return s -> s;
        return Stream
            .concat(registry.stream(), converters.stream())
            .filter((ValueParserProvider parser) -> parser.test(type))
            .map((ValueParserProvider parser) -> parser.apply(type))
            .filter(Objects::nonNull)
            .findFirst()
            .orElseGet(() -> s -> s);
    }

}