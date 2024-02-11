package org.xomda.core.module.template;

import static org.xomda.shared.exception.SneakyThrow.sneakyConsumer;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.xomda.model.Package;
import org.xomda.template.TemplateContext;

/**
 * This Template will call the {@link BasePackageTemplate#generate(Package, TemplateContext)} method
 * for each root package encountered.
 */
public class PackageTemplate extends BasePackageTemplate {

	private final AtomicBoolean bool = new AtomicBoolean();

	@Override
	public void generate(final org.xomda.model.Package pkg, final TemplateContext context) throws IOException {
		synchronized (this) {
			// 💀 dirty trickery 💀
			// when this method is first called, it will determine all packages and call generate upon then
			// but that's this method too, so when the boolean is set, leave the cannoli
			if (!bool.getAndSet(true)) {
				context.getParseResults().stream()
						.filter(org.xomda.model.Package.class::isInstance)
						.map(org.xomda.model.Package.class::cast)
						.filter(p -> p.getPackage() == null)
						.forEach(sneakyConsumer(p -> super.generate(p, context)));
			} else {
				super.generate(pkg, context);
			}
		}
	}

}