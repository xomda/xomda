package org.xomda.core.csv;

import org.xomda.core.csv.type.ValueParser;

/**
 * A single CSV "Cell" in a {@link CsvSchemaObject}.
 * It has a name and an index, and it knows how to parse a String value into the required java type.
 */
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
