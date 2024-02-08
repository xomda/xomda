package org.xomda.core.module.template;

import static org.xomda.shared.exception.SneakyThrow.sneaky;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import org.xomda.model.Entity;
import org.xomda.model.Value;
import org.xomda.template.Template;
import org.xomda.template.TemplateContext;

public class PackageTemplate implements Template<org.xomda.model.Package> {

	@Override
	public void generate(final org.xomda.model.Package pkg, final TemplateContext context) throws IOException {
		processList(pkg::getPackageList, sneaky(this::generate), context);
		processList(pkg::getEnumList, sneaky(this::generate), context);
		processList(pkg::getEntityList, sneaky(this::generate), context);
	}

	public void generate(final org.xomda.model.Enum enm, final TemplateContext context) throws IOException {
		processList(enm::getValueList, sneaky(this::generate), context);
	}

	public void generate(final Value enumValue, final TemplateContext context) throws IOException {
	}

	public void generate(final Entity entity, final TemplateContext context) throws IOException {
	}

	static <D, C extends Collection<D>> void processList(final Supplier<C> supplier, final BiConsumer<D, TemplateContext> consumer, final TemplateContext context) {
		Optional.ofNullable(supplier.get()).ifPresent(lst -> lst.forEach(it -> consumer.accept(it, context)));
	}
}
