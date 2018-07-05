package org.alixia.javalibrary;

public final class JavaTools {
	private JavaTools() {
	}

	@SafeVarargs
	public static final <T> T pickRandomElement(T... ts) {
		return ts[(int) (Math.random() * ts.length)];
	}

}
