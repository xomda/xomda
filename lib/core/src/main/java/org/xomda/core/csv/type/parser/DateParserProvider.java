package org.xomda.core.csv.type.parser;

import java.util.Date;

import org.xomda.core.csv.type.ValueParser;

public class DateParserProvider extends org.xomda.core.csv.type.parser.AbstractValueParserProvider.Nullable {

    public DateParserProvider() {
        super(createPredicate(Date.class), (ValueParser.Primitive) java.time.LocalDateTime::parse);
    }

}
