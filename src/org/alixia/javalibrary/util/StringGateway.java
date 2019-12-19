package org.alixia.javalibrary.util;

public interface StringGateway<T> extends Gateway<String, T> {
	@Override
	default String from(T value) {
		return value.toString();
	}

	static StringGateway<String> string() {
		return a -> a;
	}
}
