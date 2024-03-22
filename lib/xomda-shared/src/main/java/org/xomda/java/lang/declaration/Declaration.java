package org.xomda.java.lang.declaration;

import java.lang.reflect.Modifier;

public class Declaration {

	private static final int ACCESS_MODIFIERS = Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED;

	private int modifier = 0;
	private String type; // Object, int, but also class, interface, ...
	private String name; // the name of the object

	public int getModifier() {
		return modifier;
	}

	Declaration setModifier(final int modifier) {
		this.modifier = modifier;
		return this;
	}

	Declaration unsetModifier(final int modifier) {
		this.modifier = this.modifier & ~modifier;
		return this;
	}

	Declaration addModifier(final int modifier) {
		// if it's an access modifier, clear the previous access modifiers first
		if ((modifier & ACCESS_MODIFIERS) > 0) {
			unsetModifier(ACCESS_MODIFIERS);
		}
		this.modifier = this.modifier | modifier;
		return this;
	}

	String getType() {
		return type;
	}

	void setType(final String type) {
		this.type = type;
	}

	String getName() {
		return name;
	}

	void setName(final String name) {
		this.name = name;
	}

}
