package org.xomda.core.module;

import java.util.HashSet;

import org.xomda.core.extension.XOmdaExtension;
import org.xomda.core.extension.XOmdaModule;

public class XOmdaCore extends HashSet<Class<? extends XOmdaExtension>> implements XOmdaModule {

    public XOmdaCore() {
        super(2);
        // parse reverse entities
        add(XOmdaReverseEntity.class);
        // parse object references (ie "XOMDA/Core/Entity")
        add(XOmdaTypeRefs.class);
    }

}
