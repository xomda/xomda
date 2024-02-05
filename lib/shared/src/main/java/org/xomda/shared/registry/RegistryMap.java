package org.xomda.shared.registry;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class RegistryMap<K, V> {

	private final Map<K, V> cache = new ConcurrentHashMap<>();

	public V register(K key, V value) {
		return null == key ? null : cache.put(key, value);
	}

	public V get(K key) {
		return null == key ? null : cache.get(key);
	}

	public Optional<V> probe(K key) {
		return null == key ? Optional.empty() : Optional.of(cache.get(key));
	}

	public Stream<V> stream() {
		return cache.values().stream();
	}

}
