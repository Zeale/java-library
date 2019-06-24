package org.alixia.javalibrary.util;

import java.util.function.Function;

public interface Gateway<F, T> {
	T to(F value);

	F from(T value);

	default Function<F, T> from() {
		return this::to;
	}

	default Function<T, F> to() {
		return this::from;
	}
}
