package org.xomda.core.csv.type.parser;

import java.time.LocalDateTime;
import java.util.Date;

import org.xomda.core.csv.type.ValueParser;

public class DateParserProvider extends AbstractValueParserProvider.Nullable {

	public DateParserProvider() {
		super(createPredicate(Date.class), (ValueParser.Primitive) LocalDateTime::parse);
	}

}
