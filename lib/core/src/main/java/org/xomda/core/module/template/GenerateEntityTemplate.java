package org.xomda.core.module.template;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.xomda.core.template.TemplateContext;
import org.xomda.core.template.TemplateUtils;
import org.xomda.core.template.context.WritableContext;
import org.xomda.core.template.context.java.JavaClassWriter;
import org.xomda.model.Attribute;
import org.xomda.model.Dependency;
import org.xomda.model.Entity;
import org.xomda.model.Package;
import org.xomda.shared.util.StringUtils;

public class GenerateEntityTemplate extends PackageTemplate {

    @Override
    public void generate(final Entity entity, final TemplateContext context) throws IOException {
        final String pkg = TemplateUtils.getJavaPackage(entity.getPackage());
        final String interfaceName = StringUtils.toPascalCase(entity.getName());
        final String fullyQualifiedName = pkg + "." + interfaceName;

        try (
            @SuppressWarnings("resource")
			JavaClassWriter ctx = new JavaClassWriter(fullyQualifiedName).withHeaders(
                "// THIS FILE WAS AUTOMATICALLY GENERATED",
                "")
        ) {
            ctx
                .println("public interface " + interfaceName + " {").tab(tabbed -> tabbed
                    .println()

                    // generate the regular attributes
                    .forEach(entity::getAttributeList, (final Attribute attribute) -> {
                        final String attributeName = StringUtils.toPascalCase(attribute.getName());
                        final String fullyQualifiedType = ctx.addImport(TemplateUtils.getJavaType(attribute));
                        final String identifier = TemplateUtils.getJavaIdentifier(StringUtils.toCamelCase(attribute.getName()));
                        tabbed
                            // getter
                            .addDocs(doc -> {
                                if (StringUtils.isNullOrBlank(attribute.getDescription())) return;
                                doc.println(attribute.getDescription());
                            })
                            .println("{0} get{1}();", fullyQualifiedType, attributeName)
                            // setter
                            .addDocs(doc -> {
                                if (StringUtils.isNullOrBlank(attribute.getDescription())) return;
                                doc.println(attribute.getDescription());
                            })
                            .println("void set{0}(final {1} {2});", attributeName, fullyQualifiedType, identifier)
                            .println();
                    })

                    // generate the reverse entity attributes
                    .forEach(findReverseEntities(entity), (final Entity e) -> {
                        final String attributeName = StringUtils.toPascalCase(e.getName() + " List");
                        final CharSequence fullyQualifiedType = WritableContext.format(
                            "{0}<{1}>",
                            ctx.addImport(List.class),
                            ctx.addImport(TemplateUtils.getJavaType(e)));
                        final String identifier = TemplateUtils.getJavaIdentifier(StringUtils.toCamelCase(e.getName()));
                        tabbed
                            // getter
                            .addDocs(doc -> {
                                if (StringUtils.isNullOrBlank(entity.getDescription())) return;
                                doc.println(entity.getDescription());
                            })
                            .println("{0} get{1}();", fullyQualifiedType, attributeName)
                            // setter
                            .addDocs(doc -> {
                                if (StringUtils.isNullOrBlank(entity.getDescription())) return;
                                doc.println(entity.getDescription());
                            })
                            .println("void set{0}(final {1} {2});", attributeName, fullyQualifiedType, identifier)
                            .println();
                    }))
                .println("}");

            ctx.writeFile(context.outDir());
        }
    }

    private static Stream<Entity> findReverseEntities(final Entity entity) {
        if (null == entity) {
            return Stream.empty();
        }
        Package root = entity.getPackage();
        while (null != root.getPackage()) {
            root = root.getPackage();
        }
        return getAllEntities(entity.getPackage()).filter(e -> stream(e::getAttributeList).anyMatch(a -> Dependency.Composite.equals(a.getDependency()) && entity.equals(a.getEntityRef())));
    }

    private static Stream<Entity> getAllEntities(final Package pkg) {
        return null == pkg ? Stream.empty()
            : Stream.concat(
            stream(pkg::getPackageList).flatMap(GenerateEntityTemplate::getAllEntities),
            stream(pkg::getEntityList));
    }

    private static <T> Stream<T> stream(final Supplier<Collection<T>> supplier) {
        final Collection<T> col = supplier.get();
        return null == col ? Stream.empty() : col.stream();
    }

}