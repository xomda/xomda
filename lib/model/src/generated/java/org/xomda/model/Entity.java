// THIS FILE WAS AUTOMATICALLY GENERATED

package org.xomda.model;

import java.util.List;

public interface Entity {
	
	/**
	 * The package to which the entity belongs.
	 */
	org.xomda.model.Package getPackage();
	
	/**
	 * The package to which the entity belongs.
	 */
	void setPackage(final org.xomda.model.Package pckg);
	
	/**
	 * The name of the Entity.
	 */
	String getName();
	
	/**
	 * The name of the Entity.
	 */
	void setName(final String name);
	
	/**
	 * The unique identifier of the Entity.
	 */
	String getIdentifier();
	
	/**
	 * The unique identifier of the Entity.
	 */
	void setIdentifier(final String identifier);
	
	/**
	 * The type of Entity.
	 */
	EntityType getType();
	
	/**
	 * The type of Entity.
	 */
	void setType(final EntityType type);
	
	/**
	 * Specifies whether the Entity is persisted, or transient.
	 */
	Boolean getTransient();
	
	/**
	 * Specifies whether the Entity is persisted, or transient.
	 */
	void setTransient(final Boolean trnsnt);
	
	Dependency getDependency();
	
	void setDependency(final Dependency dependency);
	
	/**
	 * The parent Entity from which the Entity inherits.
	 */
	Entity getParent();
	
	/**
	 * The parent Entity from which the Entity inherits.
	 */
	void setParent(final Entity parent);
	
	/**
	 * A meaningful description of the Entity.
	 */
	String getDescription();
	
	/**
	 * A meaningful description of the Entity.
	 */
	void setDescription(final String description);
	
	/**
	 * Entity represents an object in the Object Model.
	 */
	List<Attribute> getAttributeList();
	
	/**
	 * Entity represents an object in the Object Model.
	 */
	void setAttributeList(final List<Attribute> attributelist);
	
}
