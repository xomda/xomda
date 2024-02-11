package org.xomda.parser;

import java.util.List;

import org.xomda.core.config.Configuration;
import org.xomda.core.extension.XOMDAExtension;
import org.xomda.parser.csv.CsvObject;

public interface ParseContext {

	Configuration getConfig();

	List<? extends XOMDAExtension> getExtensions();

	/**
	 * The complete list of parsed objects
	 */
	<T> List<T> getObjects();

	/**
	 * Adds a newly parsed object to the object cache
	 */
	void add(final CsvObject object);

	void runDeferred(final Runnable action);

}