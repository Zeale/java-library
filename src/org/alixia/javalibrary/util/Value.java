package org.alixia.javalibrary.util;

public class Value<V> {
	private final V value;

	public Value(V value) {
		this.value = value;
	}

	public V getValue() {
		return value;
	}

}
