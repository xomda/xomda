package org.xomda.core.module.template;

import static org.xomda.shared.exception.SneakyThrow.sneaky;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import org.xomda.core.template.Template;
import org.xomda.core.template.TemplateContext;
import org.xomda.model.Entity;
import org.xomda.model.Value;

public class PackageTemplate implements Template<org.xomda.model.Package> {

    @Override
    public void generate(final org.xomda.model.Package pkg, TemplateContext context) throws IOException {
        processList(pkg::getPackageList, sneaky(this::generate), context);
        processList(pkg::getEnumList, sneaky(this::generate), context);
        processList(pkg::getEntityList, sneaky(this::generate), context);
    }

    public void generate(org.xomda.model.Enum enm, TemplateContext context) throws IOException {
        processList(enm::getValueList, sneaky(this::generate), context);
    }

    public void generate(Value enumValue, TemplateContext context) throws IOException {
    }

    public void generate(Entity entity, TemplateContext context) throws IOException {
    }

    static <D, C extends Collection<D>> void processList(Supplier<C> supplier, BiConsumer<D, TemplateContext> consumer, TemplateContext context) {
        Optional
            .ofNullable(supplier.get())
            .ifPresent(lst -> lst.forEach(it -> consumer.accept(it, context)));
    }
}
