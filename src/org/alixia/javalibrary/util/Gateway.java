package org.alixia.javalibrary.util;

public interface Gateway<F, T> {
	T from(F value);

	F to(T value);
}
