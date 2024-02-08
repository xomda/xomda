package org.xomda.core.extension;

import org.xomda.parser.csv.CsvObject;

@FunctionalInterface
public interface CsvObjectProcessor extends XOmdaExtension {

	void process(CsvObject csvObject);

}
