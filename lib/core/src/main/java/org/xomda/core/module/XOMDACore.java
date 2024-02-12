package org.xomda.core.module;

import java.io.Serial;
import java.util.LinkedHashSet;

import org.xomda.core.extension.XOMDAExtension;
import org.xomda.core.extension.XOMDAModule;
import org.xomda.core.module.template.XOMDACodeTemplate;

public class XOMDACore extends LinkedHashSet<Class<? extends XOMDAExtension>> implements XOMDAModule {

	@Serial
	private static final long serialVersionUID = -287571412358053236L;

	public XOMDACore() {
		super(2);
		// parse reverse entities
		add(XOMDAReverseEntity.class);
		// parse object references (ie "XOMDA/Core/Entity")
		add(XOMDATypeRefs.class);
		// code templates
		add(XOMDACodeTemplate.class);
	}

}
