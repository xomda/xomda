package com.jorisaerts.omda.model;

import java.util.List;

public interface Package {

    String getName();

    void setName(String name);

    List<Package> getPackageList();

    void setPackageList(List<Package> packageList);

    List<Entity> getEntityList();

    void setEntityList(List<Entity> entityList);

    List<Enum> getEnumList();

    void setEnumList(List<Enum> enumList);


}
