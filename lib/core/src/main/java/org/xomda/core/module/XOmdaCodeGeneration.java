package org.xomda.core.module;

import java.util.HashSet;

import org.xomda.core.extension.XOmdaExtension;
import org.xomda.core.extension.XOmdaModule;
import org.xomda.core.module.template.GenerateEntityTemplate;
import org.xomda.core.module.template.GenerateEnumTemplate;

@SuppressWarnings("serial")
public class XOmdaCodeGeneration extends HashSet<Class<? extends XOmdaExtension>> implements XOmdaModule {
    public XOmdaCodeGeneration() {
        super(2);
        // code generation
        add(GenerateEnumTemplate.class);
        add(GenerateEntityTemplate.class);
    }

}
