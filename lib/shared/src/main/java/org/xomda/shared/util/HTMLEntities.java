package org.xomda.shared.util;

import java.io.Serial;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A map which translates characters to predefined HTML entities,
 * without "<code>&amp;</code>" or "<code>;</code>".
 */
public final class HTMLEntities extends HashMap<Integer, String> {

	@Serial
	private static final long serialVersionUID = -2371834961577005360L;

	private static final Map<Integer, String> instance;

	static {
		instance = Collections.unmodifiableMap(new HTMLEntities());
	}

	public static boolean containsKey(final Character c) {
		return instance.containsKey((int) c);
	}

	public static String get(final Character c) {
		return instance.get((int) c);
	}

	public static String translate(final Character c) {
		if (containsKey(c)) {
			return get(c);
		}
		return "#" + ((int) c);
	}

	private HTMLEntities() {
		super(192, 1);
		put(198, "AElig");
		put(193, "Aacute");
		put(194, "Acirc");
		put(192, "Agrave");
		put(197, "Aring");
		put(195, "Atilde");
		put(196, "Auml");
		put(199, "Ccedil");
		put(8225, "Dagger");
		put(208, "ETH");
		put(201, "Eacute");
		put(202, "Ecirc");
		put(200, "Egrave");
		put(203, "Euml");
		put(205, "Iacute");
		put(206, "Icirc");
		put(204, "Igrave");
		put(207, "Iuml");
		put(209, "Ntilde");
		put(211, "Oacute");
		put(212, "Ocirc");
		put(210, "Ograve");
		put(216, "Oslash");
		put(213, "Otilde");
		put(214, "Ouml");
		put(8243, "Prime");
		put(222, "THORN");
		put(218, "Uacute");
		put(219, "Ucirc");
		put(217, "Ugrave");
		put(220, "Uuml");
		put(221, "Yacute");
		put(225, "aacute");
		put(226, "acirc");
		put(180, "acute");
		put(230, "aelig");
		put(224, "agrave");
		put(8501, "alefsym");
		put(38, "amp");
		put(8743, "and");
		put(8736, "ang");
		put(229, "aring");
		put(8776, "asymp");
		put(227, "atilde");
		put(228, "auml");
		put(8222, "bdquo");
		put(166, "brvbar");
		put(8226, "bull");
		put(8745, "cap");
		put(231, "ccedil");
		put(184, "cedil");
		put(162, "cent");
		put(9827, "clubs");
		put(8773, "cong");
		put(169, "copy");
		put(8629, "crarr");
		put(8746, "cup");
		put(164, "curren");
		put(8659, "dArr");
		put(8224, "dagger");
		put(8595, "darr");
		put(176, "deg");
		put(9830, "diams");
		put(247, "divide");
		put(233, "eacute");
		put(234, "ecirc");
		put(232, "egrave");
		put(8709, "empty");
		put(8195, "emsp");
		put(8194, "ensp");
		put(8801, "equiv");
		put(240, "eth");
		put(235, "euml");
		put(8364, "euro");
		put(8707, "exist");
		put(8704, "forall");
		put(189, "frac12");
		put(188, "frac14");
		put(190, "frac34");
		put(8260, "frasl");
		put(8805, "ge");
		put(62, "gt");
		put(8660, "hArr");
		put(8596, "harr");
		put(9829, "hearts");
		put(8230, "hellip");
		put(237, "iacute");
		put(238, "icirc");
		put(161, "iexcl");
		put(236, "igrave");
		put(8465, "image");
		put(8734, "infin");
		put(8747, "int");
		put(191, "iquest");
		put(8712, "isin");
		put(239, "iuml");
		put(8656, "lArr");
		put(9001, "lang");
		put(171, "laquo");
		put(8592, "larr");
		put(8968, "lceil");
		put(8220, "ldquo");
		put(8804, "le");
		put(8970, "lfloor");
		put(8727, "lowast");
		put(9674, "loz");
		put(8206, "lrm");
		put(8249, "lsaquo");
		put(8216, "lsquo");
		put(60, "lt");
		put(175, "macr");
		put(8212, "mdash");
		put(181, "micro");
		put(183, "middot");
		put(8722, "minus");
		put(8711, "nabla");
		put(160, "nbsp");
		put(8211, "ndash");
		put(8800, "ne");
		put(8715, "ni");
		put(172, "not");
		put(8713, "notin");
		put(8836, "nsub");
		put(241, "ntilde");
		put(243, "oacute");
		put(244, "ocirc");
		put(242, "ograve");
		put(8254, "oline");
		put(8853, "oplus");
		put(8744, "or");
		put(170, "ordf");
		put(186, "ordm");
		put(248, "oslash");
		put(245, "otilde");
		put(8855, "otimes");
		put(246, "ouml");
		put(182, "para");
		put(8706, "part");
		put(8240, "permil");
		put(8869, "perp");
		put(177, "plusmn");
		put(163, "pound");
		put(8242, "prime");
		put(8719, "prod");
		put(8733, "prop");
		put(34, "quot");
		put(8658, "rArr");
		put(8730, "radic");
		put(9002, "rang");
		put(187, "raquo");
		put(8594, "rarr");
		put(8969, "rceil");
		put(8221, "rdquo");
		put(8476, "real");
		put(174, "reg");
		put(8971, "rfloor");
		put(8207, "rlm");
		put(8250, "rsaquo");
		put(8217, "rsquo");
		put(8218, "sbquo");
		put(8901, "sdot");
		put(167, "sect");
		put(173, "shy");
		put(8764, "sim");
		put(9824, "spades");
		put(8834, "sub");
		put(8838, "sube");
		put(8721, "sum");
		put(185, "sup1");
		put(178, "sup2");
		put(179, "sup3");
		put(8835, "sup");
		put(8839, "supe");
		put(223, "szlig");
		put(8756, "there4");
		put(8201, "thinsp");
		put(254, "thorn");
		put(215, "times");
		put(8482, "trade");
		put(8657, "uArr");
		put(250, "uacute");
		put(8593, "uarr");
		put(251, "ucirc");
		put(249, "ugrave");
		put(168, "uml");
		put(252, "uuml");
		put(8472, "weierp");
		put(253, "yacute");
		put(165, "yen");
		put(255, "yuml");
		put(8205, "zwj");
		put(8204, "zwnj");
	}

}
