package org.xomda.parser;

import java.util.List;

import org.xomda.core.config.Configuration;
import org.xomda.core.extension.XOmdaExtension;
import org.xomda.parser.csv.CsvObject;

public interface ParseContext {

	Configuration getConfig();

	List<? extends XOmdaExtension> getExtensions();

	/**
	 * The complete list of parsed objects
	 */
	<T> List<T> getObjects();

	void add(final CsvObject object);

	void runDeferred(final Runnable action);

}