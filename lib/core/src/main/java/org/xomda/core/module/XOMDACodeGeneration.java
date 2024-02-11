package org.xomda.core.module;

import java.io.Serial;
import java.util.HashSet;

import org.xomda.core.extension.XOMDAExtension;
import org.xomda.core.extension.XOMDAModule;
import org.xomda.core.module.template.GenerateEntityTemplate;
import org.xomda.core.module.template.GenerateEnumTemplate;

public class XOMDACodeGeneration extends HashSet<Class<? extends XOMDAExtension>> implements XOMDAModule {

	@Serial
	private static final long serialVersionUID = -1573308256999316945L;

	public XOMDACodeGeneration() {
		super(2);
		// code generation
		add(GenerateEnumTemplate.class);
		add(GenerateEntityTemplate.class);
	}

}
