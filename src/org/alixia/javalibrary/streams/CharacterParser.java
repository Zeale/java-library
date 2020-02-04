package org.alixia.javalibrary.streams;

import java.io.IOException;
import java.io.Reader;

public interface CharacterParser extends CharacterStream {
	/**
	 * Returns the last value returned by {@link #next()}, or throws an exception if
	 * none exists.
	 * 
	 * @return The current character that this parser is on.
	 */
	int curr() throws RuntimeException;

	static CharacterParser from(Reader reader) {
		return new CharacterParser() {
			int curr;

			@Override
			public int next() {
				try {
					return curr = reader.read();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public int curr() throws RuntimeException {
				return curr;
			}
		};
	}

	static CharacterParser from(CharacterStream stream) {
		return new CharacterParser() {
			int curr = -2;

			@Override
			public int next() {
				return curr = stream.next();
			}

			@Override
			public int curr() throws RuntimeException {
				return curr;
			}
		};
	}
}
