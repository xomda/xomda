package org.xomda.template;

import java.io.IOException;

import org.xomda.core.extension.Loggable;
import org.xomda.core.extension.XOMDAExtension;

@FunctionalInterface
public interface Template<T> extends XOMDAExtension, Loggable {

	void generate(T o, TemplateContext context) throws IOException;

}
