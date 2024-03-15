// THIS FILE WAS AUTOMATICALLY GENERATED

package org.xomda.model;

import java.util.List;

/**
 * A representation for a static list of values.
 */
public interface Enum {
	
	/**
	 * The package to which the enum belongs.
	 */
	org.xomda.model.Package getPackage();
	
	/**
	 * The package to which the enum belongs.
	 */
	void setPackage(final org.xomda.model.Package pckg);
	
	/**
	 * The name of the enum.
	 */
	String getName();
	
	/**
	 * The name of the enum.
	 */
	void setName(final String name);
	
	/**
	 * The unique identifier of the enum.
	 */
	String getIdentifier();
	
	/**
	 * The unique identifier of the enum.
	 */
	void setIdentifier(final String identifier);
	
	/**
	 * The description of the enum.
	 */
	String getDescription();
	
	/**
	 * The description of the enum.
	 */
	void setDescription(final String description);
	
	/**
	 * A representation for a static list of values.
	 */
	List<Value> getValueList();
	
	/**
	 * A representation for a static list of values.
	 */
	void setValueList(final List<Value> valuelist);
	
}
