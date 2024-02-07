package org.xomda.parser;

import java.util.List;

import org.xomda.parser.csv.CsvObject;

public interface InternalParseContext {

	List<CsvObject> getCache();

}
