// THIS FILE WAS AUTOMATICALLY GENERATED

package org.xomda.model;

public interface Attribute {

	Entity getEntity();

	void setEntity(final Entity entity);

	String getName();

	void setName(final String name);

	String getIdentifier();

	void setIdentifier(final String identifier);

	AttributeType getType();

	void setType(final AttributeType type);

	Long getSize();

	void setSize(final Long size);

	Long getScale();

	void setScale(final Long scale);

	org.xomda.model.Enum getEnumRef();

	void setEnumRef(final org.xomda.model.Enum enumRef);

	Entity getEntityRef();

	void setEntityRef(final Entity entityRef);

	Dependency getDependency();

	void setDependency(final Dependency dependency);

	Boolean getMultiValued();

	void setMultiValued(final Boolean multiValued);

	Boolean getRequired();

	void setRequired(final Boolean required);

	Boolean getInternal();

	void setInternal(final Boolean internal);

	String getDescription();

	void setDescription(final String description);

}
