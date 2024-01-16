package com.jorisaerts.omda.csv;

public class CsvSchemaObjectAttribute {

    private final String name;
    private final CsvTypeFactory.Setter setter;

    private final int index;

    CsvSchemaObjectAttribute(final String name, final int index, final CsvTypeFactory.Setter setter) {
        this.name = name;
        this.setter = setter;
        this.index = index;
    }


    public String getName() {
        return name;
    }

    public CsvTypeFactory.Setter getSetter() {
        return setter;
    }

    public int getIndex() {
        return index;
    }

}
