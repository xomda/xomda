package org.xomda.core.extension;

import org.xomda.core.util.ParseContext;

public interface XOmdaExtension extends Loggable {

	default void init(final ParseContext context) {
		// optional, just ignore it then
	}

}
