package org.xomda.lib.java.renderer;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.function.IntPredicate;

public class RenderUtils {

	private static boolean is(org.xomda.lib.java.ast.Modifier modifier, IntPredicate test) {
		return null != modifier && test.test(modifier.getIdentifier().intValue());
	}

	private static boolean isAny(List<org.xomda.lib.java.ast.Modifier> modifiers, IntPredicate test) {
		return modifiers.stream()
				.map(org.xomda.lib.java.ast.Modifier::getIdentifier)
				.mapToInt(Long::intValue)
				.anyMatch(test);
	}

	public static boolean isStatic(org.xomda.lib.java.ast.Modifier modifier) {
		return is(modifier, Modifier::isStatic);
	}

	public static boolean isStatic(List<org.xomda.lib.java.ast.Modifier> modifiers) {
		return isAny(modifiers, Modifier::isStatic);
	}

	public static boolean isInterface(org.xomda.lib.java.ast.Modifier modifier) {
		return is(modifier, Modifier::isInterface);
	}

	public static boolean isInterface(List<org.xomda.lib.java.ast.Modifier> modifiers) {
		return isAny(modifiers, Modifier::isInterface);
	}

}
