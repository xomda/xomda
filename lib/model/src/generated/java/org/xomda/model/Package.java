// THIS FILE WAS AUTOMATICALLY GENERATED

package org.xomda.model;

import java.util.List;

public interface Package {
  
  org.xomda.model.Package getPackage();
  void setPackage(final org.xomda.model.Package pckg);
  
  String getName();
  void setName(final String name);
  
  String getPackageName();
  void setPackageName(final String packageName);
  
  String getDescription();
  void setDescription(final String description);
  
  List<org.xomda.model.Package> getPackageList();
  void setPackageList(final List<org.xomda.model.Package> pckg);
  
  List<org.xomda.model.Enum> getEnumList();
  void setEnumList(final List<org.xomda.model.Enum> enm);
  
  List<Entity> getEntityList();
  void setEntityList(final List<Entity> entity);
  
}
