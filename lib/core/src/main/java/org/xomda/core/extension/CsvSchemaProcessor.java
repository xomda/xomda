package org.xomda.core.extension;

import org.xomda.parser.csv.CsvSchema;

@FunctionalInterface
public interface CsvSchemaProcessor extends XOmdaExtension {

	void process(CsvSchema csvSchema);

}
