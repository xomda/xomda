package org.xomda.shared.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Registry<V> {

	private final Collection<V> cache;

	public boolean register(V item) {
		return cache.add(item);
	}

	public Stream<V> stream() {
		return cache.stream();
	}

	public Registry(Supplier<Collection<V>> cacheSupplier) {
		this.cache = cacheSupplier.get();
	}

	public Registry() {
		this(ArrayList::new);
	}

}
