package org.xomda.core.csv;

import org.xomda.core.csv.type.ValueParser;

public class CsvSchemaObjectAttribute {

    private final String name;
    private final ValueParser valueParser;

    private final int index;

    CsvSchemaObjectAttribute(final String name, final int index, final ValueParser valueParser) {
        this.name = name;
        this.valueParser = valueParser;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public ValueParser getSetter() {
        return valueParser;
    }

    public int getIndex() {
        return index;
    }

}
