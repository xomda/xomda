package org.xomda.core.extension;

import org.xomda.parser.ParseContext;

/**
 * This is the XOMDA core extension.
 * It provides a logger, but nothing more.
 *
 * All the rest is left to the implementation.
 */
public interface XOMDAExtension extends Loggable {

	default void init(final ParseContext context) {
		// optional, just ignore it then
	}

}
