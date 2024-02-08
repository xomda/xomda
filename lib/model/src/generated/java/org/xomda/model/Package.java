// THIS FILE WAS AUTOMATICALLY GENERATED

package org.xomda.model;

import java.util.List;

/**
 * A container of other Package, Enum and Entity objects.
 */
public interface Package {
	
	/**
	 * The parent package, if there is one. Otherwise it&rsquo;s a root package.
	 */
	org.xomda.model.Package getPackage();
	
	/**
	 * The parent package, if there is one. Otherwise it&rsquo;s a root package.
	 */
	void setPackage(final org.xomda.model.Package pckg);
	
	/**
	 * The name of the package.
	 */
	String getName();
	
	/**
	 * The name of the package.
	 */
	void setName(final String name);
	
	/**
	 * The java package name of the package.
	 */
	String getPackageName();
	
	/**
	 * The java package name of the package.
	 */
	void setPackageName(final String packagename);
	
	/**
	 * The description of the package.
	 */
	String getDescription();
	
	/**
	 * The description of the package.
	 */
	void setDescription(final String description);
	
	/**
	 * A container of other Package, Enum and Entity objects.
	 */
	List<org.xomda.model.Package> getPackageList();
	
	/**
	 * A container of other Package, Enum and Entity objects.
	 */
	void setPackageList(final List<org.xomda.model.Package> packagelist);
	
	/**
	 * A container of other Package, Enum and Entity objects.
	 */
	List<org.xomda.model.Enum> getEnumList();
	
	/**
	 * A container of other Package, Enum and Entity objects.
	 */
	void setEnumList(final List<org.xomda.model.Enum> enumlist);
	
	/**
	 * A container of other Package, Enum and Entity objects.
	 */
	List<Entity> getEntityList();
	
	/**
	 * A container of other Package, Enum and Entity objects.
	 */
	void setEntityList(final List<Entity> entitylist);
	
}
