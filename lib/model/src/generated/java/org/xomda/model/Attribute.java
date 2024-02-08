// THIS FILE WAS AUTOMATICALLY GENERATED

package org.xomda.model;

/**
 * An attribute of an entity.
 */
public interface Attribute {
	
	/**
	 * The entity to which the attribute belongs.
	 */
	Entity getEntity();
	
	/**
	 * The entity to which the attribute belongs.
	 */
	void setEntity(final Entity entity);
	
	/**
	 * The name of the attribute.
	 */
	String getName();
	
	/**
	 * The name of the attribute.
	 */
	void setName(final String name);
	
	/**
	 * The unique identifier of the Attribute.
	 */
	String getIdentifier();
	
	/**
	 * The unique identifier of the Attribute.
	 */
	void setIdentifier(final String identifier);
	
	/**
	 * The type of Attribute.
	 */
	AttributeType getType();
	
	/**
	 * The type of Attribute.
	 */
	void setType(final AttributeType type);
	
	/**
	 * The size of the attribute, for String or Text types.
	 */
	Long getSize();
	
	/**
	 * The size of the attribute, for String or Text types.
	 */
	void setSize(final Long size);
	
	Long getScale();
	
	void setScale(final Long scale);
	
	/**
	 * The reference to an enum value, if the type is Enum.
	 */
	org.xomda.model.Enum getEnumRef();
	
	/**
	 * The reference to an enum value, if the type is Enum.
	 */
	void setEnumRef(final org.xomda.model.Enum enumref);
	
	/**
	 * The reference to an entity value, if the type is Entity.
	 */
	Entity getEntityRef();
	
	/**
	 * The reference to an entity value, if the type is Entity.
	 */
	void setEntityRef(final Entity entityref);
	
	Dependency getDependency();
	
	void setDependency(final Dependency dependency);
	
	/**
	 * Specifies whether the attribute is multi&#45;valued or not.
	 */
	Boolean getMultiValued();
	
	/**
	 * Specifies whether the attribute is multi&#45;valued or not.
	 */
	void setMultiValued(final Boolean multivalued);
	
	/**
	 * Specifies whether the attribute is required or not.
	 */
	Boolean getRequired();
	
	/**
	 * Specifies whether the attribute is required or not.
	 */
	void setRequired(final Boolean required);
	
	String getDescription();
	
	void setDescription(final String description);
	
}
