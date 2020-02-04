package org.alixia.javalibrary.streams;

public interface PeekableCharacterStream extends CharacterStream {
	int peek();

	Character peekChar();

	static PeekableCharacterStream from(CharacterStream parser) {
		return new PeekableCharacterStream() {
			private int peeked = -2;

			@Override
			public Character peekChar() {
				int p = peek();
				return p == -1 ? null : (char) p;

			}

			@Override
			public int peek() {
				return peeked == -2 ? peeked = parser.next() : peeked;
			}

			@Override
			public int next() {
				if (peeked == -2)
					return parser.next();
				else {
					int p = peeked;
					peeked = -2;
					return p;
				}

			}
		};
	}
}
