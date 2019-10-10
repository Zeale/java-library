package org.alixia.javalibrary.streams;

import java.io.IOException;
import java.io.Reader;

public interface CharacterStream {
	default Character nextChar() {
		int nextch = next();
		return nextch < 0 ? null : (Character) (char) nextch;
	}

	/**
	 * Returns the next character as an <code>int</code>, unless the previously
	 * retreived character was the last, in which case this method returns
	 * <code>-1</code>.
	 * 
	 * @return The next character, or <code>-1</code> if there is no next character.
	 */
	int next();

	static CharacterParser from(String string) {
		return new CharacterParser() {

			private int pos = 0;

			@Override
			public int next() {
				return pos >= string.length() ? -1 : string.codePointAt(pos++);
			}

			@Override
			public int curr() throws RuntimeException {
				return string.codePointAt(pos);
			}
		};
	}

	/**
	 * Returns a {@link CharacterStream} that wraps a {@link Reader}. The stream
	 * will delegate its methods to the reader's {@link Reader#read()} method,
	 * throwing a {@link RuntimeException} that wraps any checked exception thrown
	 * by the call to {@link Reader#read()}.
	 * 
	 * @param reader The {@link Reader} to wrap.
	 * @return A new {@link CharacterStream}.
	 */
	static CharacterStream from(Reader reader) {
		return () -> {
			try {
				return reader.read();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		};
	}

}
