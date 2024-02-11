package org.xomda.core.module;

import java.io.Serial;
import java.util.HashSet;

import org.xomda.core.extension.XOMDAExtension;
import org.xomda.core.extension.XOmdaModule;

public class XOMDACore extends HashSet<Class<? extends XOMDAExtension>> implements XOmdaModule {

	@Serial
	private static final long serialVersionUID = -287571412358053236L;

	public XOMDACore() {
		super(2);
		// parse reverse entities
		add(XOMDAReverseEntity.class);
		// parse object references (ie "XOMDA/Core/Entity")
		add(XOMDATypeRefs.class);
	}

}
