package org.alixia.javalibrary.util;

import java.io.Serializable;
import java.util.Map;
import java.util.WeakHashMap;

public class KeyMap<V, M extends Map<KeyMap.Key<?>, V>> implements Serializable {

	/**
	 * SUID
	 */
	private static final long serialVersionUID = 1L;

	private final M data;

	protected KeyMap(M map) {
		this.data = map;
	}

	public static <V, M extends Map<Key<?>, V>> KeyMap<V, M> keyMap(M map) {
		return new KeyMap<>(map);
	}

	public static <V> KeyMap<V, WeakHashMap<KeyMap.Key<?>, V>> keyMap() {
		return new KeyMap<>(new WeakHashMap<>());
	}

	/**
	 * Returns the {@link WeakHashMap} that backs this {@link KeyMap}. Care should
	 * be taken when adding data to the map via the raw {@link WeakHashMap} returned
	 * by this method, as not honoring the type parameters laced with variables
	 * holding {@link Key} objects may cause problems when retrieving values using
	 * those {@link Key} objects.
	 * 
	 * @return The {@link WeakHashMap} that backs this {@link KeyMap}.
	 */
	public M getData() {
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

	@SuppressWarnings("unchecked")
	public <T extends V> T put(Key<T> key, T data) {
		return (T) this.data.put(key, data);
	}

	@SuppressWarnings("unchecked")
	public <T extends V> T get(Key<T> key) {
		return (T) data.get(key);
	}

	public boolean containsKey(Key<?> key) {
		return data.containsKey(key);
	}

	public <T extends V> Key<T> put(T data) {
		Key<T> key = key();
		put(key, data);
		return key;
	}

	protected static <V> Key<V> key() {
		return new Key<>();
	}

	public <KV extends V> LocalKey<KV> lk(Key<KV> key) {
		return new LocalKey<>(key);
	}

	public <KV extends V> LocalKey<KV> lk() {
		return lk(new Key<>());
	}

	public static class Key<KV> {
		private Key() {
		}

		public KV get(KeyMap<? super KV, ?> map) {
			return map.get(this);
		}

		public KV put(KeyMap<? super KV, ?> map, KV data) {
			return map.put(this, data);
		}
	}

	public static class OptionalKey<KV> extends Key<KV> {
		public boolean exists(KeyMap<?, ?> map) {
			return map.containsKey(this);
		}
	}

	public class LocalKey<KV extends V> {
		public final Key<KV> key;

		public LocalKey(Key<KV> key) {
			this.key = key;
		}

		public KV get() {
			return key.get(KeyMap.this);
		}

		@SuppressWarnings("unchecked")
		public KV put(KV data) {
			return (KV) KeyMap.this.data.put(key, data);
		}

	}

}
