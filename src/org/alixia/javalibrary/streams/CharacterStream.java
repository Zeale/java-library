package org.alixia.javalibrary.streams;

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

	static CharacterStream from(String string) {
		return new CharacterStream() {

			private int pos = 0;

			@Override
			public int next() {
				return pos >= string.length() ? -1 : string.codePointAt(pos++);
			}
		};
	}
}
