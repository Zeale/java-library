package org.alixia.javalibrary.util;

public interface StringGateway<T> extends Gateway<String, T> {
	@Override
	default String to(T value) {
		return value.toString();
	}
}
