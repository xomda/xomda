package org.xomda.core.csv.type.parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.xomda.core.csv.type.ValueParser;

public class EnumParserProvider extends AbstractValueParserProvider.Nullable {

	public EnumParserProvider() {
		super(Class::isEnum, null);
	}

	@Override
	public ValueParser.Primitive apply(final Class<?> c) {
		return getEnumMap(c)::get;
	}

	private static Map<String, Object> getEnumMap(final Class<?> enumClazz) {
		if (!enumClazz.isEnum()) {
			return Collections.emptyMap();
		}
		final Object[] enumValues = enumClazz.getEnumConstants();
		try {
			final Method m = enumClazz.getMethod("name");
			return Stream.of(enumValues).collect(Collectors.toMap(v -> {
				try {
					return (String) m.invoke(v);
				} catch (final IllegalAccessException | InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}, Function.identity()));
		} catch (final NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
}
