package zeale.mouse.utils;

import java.io.IOException;
import java.io.Reader;

public interface CharacterParser extends Parser<Character> {
	int nxt();

	int pk();

	@Override
	default Character next() {
		int nxt = nxt();
		return nxt == -1 ? null : (char) nxt;
	}

	@Override
	default Character peek() {
		int pk = pk();
		return pk == -1 ? null : (char) pk;
	}

	static CharacterParser from(CharSequence seq) {
		return new CharacterParser() {
			int i = -1;

			@Override
			public int pk() {
				return i + 1 >= seq.length() ? -1 : seq.charAt(i + 1);
			}

			@Override
			public int nxt() {
				return i + 1 >= seq.length() ? -1 : seq.charAt(++i);
			}
		};
	}

	static CharacterParser from(Reader in) {
		return new CharacterParser() {
			Integer cache;

			@Override
			public int pk() {
				if (cache == null)
					try {
						return cache = in.read();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				else
					return cache;
			}

			@Override
			public int nxt() {
				try {
					if (cache == null)
						return in.read();
					else {
						int c = cache;
						cache = null;
						return c;
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		};
	}

	static CharacterParser from(Parser<Character> in) {
		return new CharacterParser() {

			@Override
			public int pk() {
				Character c = in.peek();
				return c == null ? -1 : c;
			}

			@Override
			public int nxt() {
				Character c = in.next();
				return c == null ? -1 : c;
			}
		};
	}
}
