package org.xomda.core.template;

import java.io.IOException;

import org.xomda.core.extension.Loggable;
import org.xomda.core.extension.XOmdaExtension;

@FunctionalInterface
public interface Template<T> extends XOmdaExtension, Loggable {

    void generate(T o, org.xomda.core.template.TemplateContext context) throws IOException;

}
