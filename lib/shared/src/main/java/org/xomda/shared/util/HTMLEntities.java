package org.xomda.shared.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A map which translates characters to predefined HTML entities,
 * without "<code>&amp;</code>" or "<code>;</code>".
 */
public final class HTMLEntities extends HashMap<Character, String> {

	private static final long serialVersionUID = -2371834961577005360L;

	private static final Map<Character, String> instance;

	static {
		instance = Collections.unmodifiableMap(new HTMLEntities());
	}

	public static boolean containsKey(final Character c) {
		return instance.containsKey(c);
	}

	public static String get(final Character c) {
		return instance.get(c);
	}

	public static String translate(final Character c) {
		if (containsKey(c)) {
			return get(c);
		}
		return "#" + ((int) c);
	}

	private HTMLEntities() {
		put('<', "lt");
		put('>', "gt");
		put(' ', "nbsp");
		put('&', "amp");
		put('"', "quot");
		put('\'', "apos");
		put('¢', "cent");
		put('£', "pound");
		put('¥', "yen");
		put('€', "euro");
		put('©', "copy");
		put('®', "reg");
	}

}
