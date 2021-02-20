package org.alixia.javalibrary.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MultidimensionalMap<V> {
	private final int size;

	private final Map<?, ?> root = new HashMap<>();

	public MultidimensionalMap(int size) {
		this.size = size;
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	public <K, V> Iterator<Entry<K, V>> iterator() {
		return (Iterator<Entry<K, V>>) (Iterator<?>) root.entrySet().iterator();
	}

	@SuppressWarnings("hiding")
	public <K, V> Iterable<Entry<K, V>> iterable() {
		return MultidimensionalMap.this::iterator;
	}

	private V read(Object... keys) {
		Map<?, V> map = readMap(keys);
		return map == null ? null : map.get(keys[keys.length - 1]);
	}

	/**
	 * Returns the map at the specified level, if there is one. This can be used to
	 * get a layer of this {@link MultidimensionalMap}. This method accepts any
	 * number of {@link Object} keys up to <code>{@link #size} - 1</code>.
	 * 
	 * @param keys The keys for the layer.
	 * @return The layer, as a {@link Map}.
	 */
	@SuppressWarnings("unchecked")
	public Map<?, ?> readDim(Object... keys) {
		if (keys == null || keys.length > size)
			throw new IllegalArgumentException("Illegal map access.");
		Object curr = root;
		for (int i = 0; i < keys.length; i++)
			if (((Map<?, V>) curr).containsKey(keys[i]))
				curr = ((Map<?, Map<?, ?>>) curr).get(keys[i]);
			else
				return null;
		return (Map<?, ?>) curr;
	}

	@SuppressWarnings("unchecked")
	private Map<?, V> readMap(Object... keys) {
		if (keys == null || keys.length > size)
			throw new IllegalArgumentException("Illegal map access.");
		Object curr = root;
		for (int i = 0; i < keys.length - 1; i++)
			if (((Map<?, V>) curr).containsKey(keys[i]))
				curr = ((Map<?, V>) curr).get(keys[i]);
			else
				return null;
		return (Map<?, V>) curr;
	}

	@SuppressWarnings("unchecked")
	private Map<?, V> getMap(Object... keys) {
		if (keys == null || keys.length > size)
			throw new IllegalArgumentException("Illegal map access.");
		Object curr = root;
		for (int i = 0; i < keys.length - 1; i++)
			if (((Map<?, V>) curr).containsKey(keys[i]))
				curr = ((Map<?, V>) curr).get(keys[i]);
			else
				((Map<Object, Object>) curr).put(keys[i], (curr = new HashMap<>()));
		return (Map<?, V>) curr;
	}

	public boolean contains(Object... keys) {
		Map<?, V> map = readMap(keys);
		return map != null && map.containsKey(keys[keys.length - 1]);
	}

	@SuppressWarnings("unchecked")
	private V write(V value, Object... keys) {
		return ((Map<Object, V>) getMap(keys)).put(keys[keys.length - 1], value);
	}

	public V put(V value, Object... keys) {
		return write(value, keys);
	}

	@SuppressWarnings("unchecked")
	public V remove(Object... keys) {
		return ((Map<Object, V>) readMap(keys)).remove(keys[keys.length - 1]);
	}

	public V get(Object... keys) {
		return read(keys);
	}

}
