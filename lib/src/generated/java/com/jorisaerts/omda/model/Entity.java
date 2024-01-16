package com.jorisaerts.omda.model;

import java.util.List;

public interface Entity {

    String getName();

    void setName(String name);

    List<Attribute> getAttributeList();

    void setAttributeList(List<Attribute> attributeList);

    Dependency getDependency();

    void setDependency(Dependency dependency);

}
