package com.jorisaerts.omda.model;

import java.util.List;

public interface Enum {

    String getName();

    void setName(String name);

    List<Value> getValueList();

    void setValueList(List<Value> attributeList);

}
