package org.alixia.javalibrary.util;

import java.io.Serializable;
import java.util.WeakHashMap;

public class KeyMap<V> implements Serializable {

	/**
	 * SUID
	 */
	private static final long serialVersionUID = 1L;

	private final WeakHashMap<Key<?>, V> data = new WeakHashMap<>();

	/**
	 * Returns the {@link WeakHashMap} that backs this {@link KeyMap}. Care should
	 * be taken when adding data to the map via the raw {@link WeakHashMap} returned
	 * by this method, as not honoring the type parameters laced with variables
	 * holding {@link Key} objects may cause problems when retrieving values using
	 * those {@link Key} objects.
	 * 
	 * @return The {@link WeakHashMap} that backs this {@link KeyMap}.
	 */
	public WeakHashMap<Key<?>, V> getData() {
		return data;
	}

	/**
	 * Checks if this {@link KeyMap} can be {@link Serializable serialized} by
	 * checking if all values are {@link Serializable}.
	 * 
	 * @return <code>true</code> if every value in {@link #data} is an
	 *         {@code instanceof} {@link Serializable}.
	 */
	public boolean isSerializable() {
		for (Object o : data.values())
			if (!(o instanceof Serializable))
				return false;
		return true;
	}

	public <T extends V> Key<T> put(T data) {
		Key<T> key = new Key<>();
		key.put(data);
		return key;
	}

	public class Key<KV extends V> {
		private Key() {
		}

		@SuppressWarnings("unchecked")
		public KV get() {
			return (KV) data.get(this);
		}

		@SuppressWarnings("unchecked")
		public KV put(KV data) {
			return (KV) KeyMap.this.data.put(this, data);
		}
	}

}
