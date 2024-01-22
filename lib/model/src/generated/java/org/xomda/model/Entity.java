// THIS FILE WAS AUTOMATICALLY GENERATED

package org.xomda.model;

import java.util.List;

public interface Entity {
  
  org.xomda.model.Package getPackage();
  void setPackage(final org.xomda.model.Package pckg);
  
  String getName();
  void setName(final String name);
  
  String getIdentifier();
  void setIdentifier(final String identifier);
  
  EntityType getType();
  void setType(final EntityType type);
  
  Boolean getTransient();
  void setTransient(final Boolean trnsnt);
  
  Dependency getDependency();
  void setDependency(final Dependency dependency);
  
  Entity getParent();
  void setParent(final Entity parent);
  
  String getDescription();
  void setDescription(final String description);
  
  List<Attribute> getAttributeList();
  void setAttributeList(final List<Attribute> attribute);
  
}
