package org.alixia.javalibrary.util;

import java.util.Map;
import java.util.WeakHashMap;

import org.alixia.javalibrary.util.KeyMap.Key;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class ObservableKeyMap<V, M extends ObservableMap<Key<? extends V>, V>> extends KeyMap<V, M> {

	public ObservableKeyMap(M map) {
		super(map);
	}

	public static <V, M extends ObservableMap<Key<? extends V>, V>> ObservableKeyMap<V, M> observableKeyMap(M map) {
		return new ObservableKeyMap<>(map);
	}

	public static <V> ObservableKeyMap<V, ObservableMap<KeyMap.Key<? extends V>, V>> observableKeyMap() {
		return observableKeyMap(new WeakHashMap<>());
	}

	public static <V> ObservableKeyMap<V, ObservableMap<KeyMap.Key<? extends V>, V>> observableKeyMap(
			Map<Key<? extends V>, V> map) {
		return observableKeyMap(FXCollections.observableMap(map));
	}

	// TODO

	/**
	 * SUID
	 */
	private static final long serialVersionUID = 1L;

}
