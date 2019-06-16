package org.alixia.javalibrary.util;

public class ImmutableBox<T> {
	public final T value;

	public ImmutableBox(T value) {
		this.value = value;
	}
}
