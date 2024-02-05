package org.xomda.core.extension;

import org.xomda.core.csv.CsvObject;

@FunctionalInterface
public interface CsvObjectProcessor extends XOmdaExtension {

	void process(CsvObject csvObject);

}
