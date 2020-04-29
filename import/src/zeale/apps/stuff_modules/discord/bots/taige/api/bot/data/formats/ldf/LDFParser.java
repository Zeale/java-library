package zeale.apps.stuff_modules.discord.bots.taige.api.bot.data.formats.ldf;

import java.util.ArrayList;
import java.util.List;

import org.alixia.javalibrary.streams.CharacterStream;
import org.alixia.javalibrary.streams.PeekableCharacterStream;

public final class LDFParser {

	public final static class LDFException extends RuntimeException {

		/**
		 * SUID
		 */
		private static final long serialVersionUID = 1L;

		public LDFException() {
		}

		public LDFException(String message) {
			super(message);
		}

		public LDFException(String message, Throwable cause) {
			super(message, cause);
		}

		public LDFException(Throwable cause) {
			super(cause);
		}

	}

	/**
	 * Works quite similarly to {@link #ws(CharacterStream)}; peeks at the next
	 * character; if it's whitespace, {@link CharacterStream#next()} is called so
	 * that that whitespace character is "skipped over." (Technically, this method
	 * will leave the "current" position of the stream, on the last whitespace
	 * character before non-whitespace is found, but {@link CharacterStream} doesn't
	 * let you access the current character.) <b>This method leaves off one position
	 * <i>before</i> {@link #ws(CharacterStream)} would</b>, so that a call to
	 * {@link CharacterStream#next()} will return the same character as is returned
	 * by this method.
	 *
	 * @param in The {@link CharacterStream} to consume the whitespace of.
	 */
	private int cws(PeekableCharacterStream in) {
		while (Character.isWhitespace(in.peek()))
			in.next();
		return in.peek();
	}

	/**
	 * Parses an entire LDF file.
	 *
	 * @param in The {@link CharacterStream} to parse from. The state that this
	 *           method leaves this {@link CharacterStream} in upon completion or
	 *           termination is not guaranteed to be anything in particular.
	 * @return A (possibly empty) {@link List} of all the parsed
	 *         {@link LDFSection}s.
	 */
	public List<LDFSection> parse(CharacterStream in) {
		final List<LDFSection> sections = new ArrayList<>();
		LDFSection s;
		final PeekableCharacterStream peeker = PeekableCharacterStream.from(in);

		while ((s = parseSection(peeker)) != null)
			sections.add(s);

		return sections;

	}

	private String parseEggName(PeekableCharacterStream in) {
		// Expects peek to return first part of egg.
		if (!(Character.isLetter(in.peek()) || Character.isDigit(in.peek()) || in.peek() == '_'))
			throw new LDFException(
					"Found an illegal character, (" + (char) in.peek() + "), while parsing an egg's name.");
		final StringBuilder b = new StringBuilder();
		while (Character.isLetter(in.peek()) || Character.isDigit(in.peek()) || in.peek() == '_')
			b.append((char) in.next());
		return b.toString();
	}

	private Object parseEggValue(PeekableCharacterStream in) {
		int c = cws(in);
		boolean negate;

		if (c == '!') {
			in.next();
			c = cws(in);// Whitespace is allowed after the '!'
			negate = true;
		} else
			negate = false;

		Object result;

		if (c == '"') {
			in.next();// Skip over the quote we just parsed (wouldn't wanna break out early).
			boolean escaped = false;
			final StringBuilder val = new StringBuilder();
			while (true) {
				c = in.peek();
				if (c == '"')
					if (escaped) {
						val.append('"');
						escaped = false;
					} else {
						result = val.toString();
						in.next();
						break;
					}
				else if (c == '\\') {
					if (!(escaped ^= true))
						val.append('\\');
				} else if (c == -1)
					throw new LDFException("Found the end of input while parsing a string.");
				else
					val.append((char) c);
				in.next();
			}
		} else if (c == '[') {
			in.next();
			if ((c = cws(in)) == ']')
				result = new Object[0];
			else {
				final List<Object> objs = new ArrayList<>();
				while (true) {
					objs.add(parseEggValue(in));
					c = cws(in);
					if (c == ']') {
						in.next();
						result = objs.toArray();
						break;
					} else if (c == ',') {
						in.next();
						cws(in);
					} else
						throw new LDFException(c == -1 ? "End of input reached while parsing array."
								: "Unexpected character found while parsing array: " + (char) c + '.');
				}
			}
		} else
			throw new LDFException("Found an illegal character while parsing an egg value: " + (char) c + '.');

		return negate ? new LDFNegation(result) : result;
	}

	private LDFItem parseItem(PeekableCharacterStream in) {
		int c = cws(in);
		if (c == -1 || c == '#')
			return null;
		else if (Character.isLetter(c) || Character.isDigit(c) || c == '_' || c == ' ') {
			final StringBuilder b = new StringBuilder().append((char) c);
			in.next();
			while (Character.isLetter(c = in.peek()) || Character.isDigit(c) || c == '_' || c == ' ')
				b.append((char) in.next());
			if (c == '(') {
				in.next();
				final LDFItem res = new LDFItem(b.toString().trim());
				return parseNest(in, res);
			} else if (c == -1 || Character.getType(c) == Character.LINE_SEPARATOR
					|| Character.getType(c) == Character.CONTROL)
				return new LDFItem(b.toString().trim());
			else
				throw new LDFException(
						"Encountered an unexpected character while parsing an LDF item: " + (char) c + '.');
		} else
			throw new LDFException(
					"Encountered an unexpected character where the first character in the name of an LDF item was expected: "
							+ (char) c + '.');
	}

	private LDFItem parseNest(PeekableCharacterStream in, LDFItem currentItem) {
		while (true) {
			cws(in);
			final String i = parseEggName(in);
			int c = cws(in);
			in.next();
			if (c == '=') {
				currentItem.getProperties().put(i, parseEggValue(in));
				c = cws(in);
				if (c == ',')
					in.next();
				else if (c == ')') {
					in.next();
					return currentItem;
				} else
					throw new LDFException("Unexpected character found after a value: " + (char) c + '.');
			} else if (c == ',')
				currentItem.getProperties().put(i, null);
			else if (c == ')')
				return currentItem;
			else
				throw new LDFException(c == -1 ? "End of input encountered while parsing the name of an egg."
						: "Unexpected character encountered while parsing the name of an egg: " + (char) c);
		}
	}

	/**
	 * <p>
	 * Parses an {@link LDFSection} out of the given
	 * {@link PeekableCharacterStream}. The {@link PeekableCharacterStream} can
	 * either return the <code>#</code> that opens the section, or whitespace before
	 * that <code>#</code>, upon the next call to {@link CharacterStream#next()}.
	 * </p>
	 * <p>
	 * This method will return <code>null</code> if it simply reaches the end of the
	 * file before it finds the <code>#</code> that opens the section.
	 * </p>
	 *
	 * @param in The {@link PeekableCharacterParser} to read from.
	 * @return The parsed {@link LDFSection}, or <code>null</code> if the end of the
	 *         input is reached before the beginning of the {@link LDFSection}.
	 */
	public LDFSection parseSection(PeekableCharacterStream in) {
		int n = ws(in);
		if (n == '#') {
			n = ws(in);
			if (n == -1)
				throw new LDFException("Found end of input right after a '#'; a name for a section was expected.");
			else if (!(Character.isAlphabetic(n) || Character.isDigit(n) || n == '_'))
				throw new LDFException(
						"Found an invalid character where an identifier, (at the name of a section), was expected: "
								+ (char) n + '.');
			else {
				final StringBuilder name = new StringBuilder().append((char) n);
				while (Character.isAlphabetic(n = in.next()) || Character.isDigit(n) || n == '_' || n == ' ')
					name.append((char) n);// Parse sec name

				final String res = name.toString().trim();// Remove trailing whitespace.

				if (Character.isWhitespace(n) && Character.getType(n) != Character.LINE_SEPARATOR
						&& Character.getType(n) != Character.CONTROL)
					while (Character.isWhitespace(in.peek()) && Character.getType(n) != Character.LINE_SEPARATOR
							&& Character.getType(n) != Character.CONTROL)
						in.next();// Parse out any whitespace that is non-' ' and non-line sep.

				n = in.peek();
				if (Character.getType(n) == Character.LINE_SEPARATOR || Character.getType(n) == Character.CONTROL) {
					in.next();
					while (Character.getType(n = in.peek()) == Character.LINE_SEPARATOR
							|| Character.getType(n) == Character.CONTROL)
						n = in.next();

					final List<LDFItem> items = new ArrayList<>();
					LDFItem item;

					while ((item = parseItem(in)) != null)
						items.add(item);

					return new LDFSection(res, items);

				} else
					throw new LDFException("Found an illegal character after the name of a Section: " + (char) n + '.');

			}
		} else if (n == -1)
			return null;
		else
			throw new LDFException(
					"Found an invalid character while looking for the opening '#' of a section: " + (char) n + '.');

	}

	private int ws(CharacterStream in) {
		int c;
		while (Character.isWhitespace(c = in.next()))
			;
		return c;
	}

}
