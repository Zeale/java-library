package org.alixia.javalibrary.util;

public class OptionalValue<V> extends Value<V> {

	private final boolean present;

	public OptionalValue(V value) {
		super(value);
		present = true;
	}

	public OptionalValue() {
		super(null);
		present = false;
	}

	public boolean isPresent() {
		return present;
	}

}
