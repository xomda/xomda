package org.xomda.shared.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Registry<V> {

	private final Collection<V> cache;

	public boolean register(final V item) {
		return cache.add(item);
	}

	public Stream<V> stream() {
		return cache.stream();
	}

	public Registry(final Supplier<Collection<V>> cacheSupplier) {
		cache = cacheSupplier.get();
	}

	public Registry() {
		this(ArrayList::new);
	}

}
