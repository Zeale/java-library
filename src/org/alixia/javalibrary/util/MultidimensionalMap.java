package org.alixia.javalibrary.util;

import java.util.HashMap;
import java.util.Map;

public class MultidimensionalMap<V> {
	private final int size;

	private final Map<?, ?> root = new HashMap<>();

	public MultidimensionalMap(int size) {
		this.size = size;
	}

	private V read(Object... keys) {
		Map<?, V> map = readMap(keys);
		return map == null ? null : map.get(keys[keys.length - 1]);
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
