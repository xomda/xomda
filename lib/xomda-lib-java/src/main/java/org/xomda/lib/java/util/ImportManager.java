package org.xomda.lib.java.util;

import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.xomda.lib.java.ast.Import;
import org.xomda.lib.java.ast.Type;
import org.xomda.lib.java.ast.impl.ImportImpl;
import org.xomda.lib.java.ast.impl.TypeImpl;

public class ImportManager {

	Set<Import> importSet = new ConcurrentSkipListSet<>(Comparator
			.comparing(Import::getIdentifier)
			.thenComparing(anImport -> anImport.getModifier().getIdentifier())
	);

	public void addImport(String fullyQualifiedClassName) {
		Type type = new TypeImpl();
		type.setIdentifier(fullyQualifiedClassName);

		Import imp = new ImportImpl() {
			@Override
			public String getIdentifier() {
				return type.getIdentifier();
			}
		};

		//Import imp = new ImportImpl();
		imp.setIdentifier(fullyQualifiedClassName);

		importSet.add(imp);

	}

}
