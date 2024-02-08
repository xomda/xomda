// THIS FILE WAS AUTOMATICALLY GENERATED

package org.xomda.model;

/**
 * A single (static) value of an enum.
 */
public interface Value {
	
	/**
	 * The enum to which the value belongs.
	 */
	org.xomda.model.Enum getEnum();
	
	/**
	 * The enum to which the value belongs.
	 */
	void setEnum(final org.xomda.model.Enum enm);
	
	/**
	 * The unique identifier for the value.
	 */
	Long getIdentifier();
	
	/**
	 * The unique identifier for the value.
	 */
	void setIdentifier(final Long identifier);
	
	/**
	 * The name of the value.
	 */
	String getName();
	
	/**
	 * The name of the value.
	 */
	void setName(final String name);
	
	/**
	 * The description of the value.
	 */
	String getDescription();
	
	/**
	 * The description of the value.
	 */
	void setDescription(final String description);
	
}
