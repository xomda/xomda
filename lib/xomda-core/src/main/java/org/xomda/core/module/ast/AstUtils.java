package org.xomda.core.module.ast;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.xomda.lib.java.ast.Modifier;
import org.xomda.lib.java.ast.Type;
import org.xomda.lib.java.ast.impl.ModifierImpl;
import org.xomda.lib.java.ast.impl.TypeImpl;
import org.xomda.model.Attribute;
import org.xomda.model.AttributeType;

public class AstUtils {

	static org.xomda.lib.java.ast.Modifier createModifier(int value) {
		org.xomda.lib.java.ast.Modifier mod = new ModifierImpl();
		mod.setIdentifier((long) value);
		return mod;
	}

	static List<Modifier> createModifiers(int... value) {
		return Arrays.stream(value)
				.mapToObj(AstUtils::createModifier)
				.toList();
	}

	static Type createType(final Attribute attribute) {

		final AttributeType attType = attribute.getType();

		Supplier<String> identifierSupplier;

		if (null == attType) {
			identifierSupplier = () -> "java.lang.Object";
		} else {
			identifierSupplier = (switch (attType) {
				case String, Text -> () -> "java.lang.String";
				case Boolean -> () -> "java.lang.Boolean";
				case Integer -> () -> "java.lang.Long";
				case Decimal -> () -> "java.lang.Double";
				case Date, Time, Timestamp -> () -> "java.util.Date";
				//case Entity -> getJavaInterfaceName(attribute.getEntityRef());
				//case Enum -> getJavaType(attribute.getEnumRef());
				default -> () -> "java.lang.Object";
			});
		}

		Type tp = new TypeImpl() {
			@Override
			public String getIdentifier() {
				return identifierSupplier.get();
			}
		};

		if (Boolean.TRUE.equals(attribute.getMultiValued())) {
			Type mv = new TypeImpl();
			mv.setTypeList(List.of(mv));
			mv.setIdentifier("java.util.List");
			mv.setTypeList(List.of(tp));
			return mv;
		} else {
			return tp;
		}
	}
}
