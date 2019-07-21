package org.alixia.javalibrary.util;

public interface FallibleConsumer<T> {
	void consume(T input) throws Exception;
}
