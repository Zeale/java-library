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

	default Gateway<T, F> inverse() {
		return new Gateway<T, F>() {

			@Override
			public F to(T value) {
				return Gateway.this.from(value);
			}

			@Override
			public T from(F value) {
				return Gateway.this.to(value);
			}
		};
	}
}
