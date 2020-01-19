package org.alixia.javalibrary.json;

import org.alixia.javalibrary.streams.CharacterStream;

public class JSONParser {

	private int parseWhitespace(CharacterStream stream) {
		int c;
		while (Character.isWhitespace(c = stream.next()))
			;
		return c;
	}

	private static boolean isDigit1_9(char digit) {
		return digit <= '9' && digit >= 1;
	}

	private static boolean isHexDigit(char digit) {
		return Character.isDigit(digit) || digit <= 'F' && digit >= 'A' || digit <= 'f' && digit >= 'a';
	}

	/**
	 * Parses the portion of a string immediately following the opening quotation
	 * mark (<code>"</code>). This method leaves off such that the stream's
	 * {@link CharacterStream#next() next character} is the character immediately
	 * following the closing quotation of this string.
	 * 
	 * @param stream The stream whose {@link CharacterStream#next() next character}
	 *               should be the character immediately following the opening
	 *               quotation mark.
	 * @return The parsed {@link String}.
	 */
	private JSONString parseHeadlessString(CharacterSequence stream) {
		StringBuilder builder = new StringBuilder();
		int c;
		boolean escaped = false;
		while (true) {
			c = stream.next();
			if (c == '"')
				if (escaped)
					builder.append('"');
				else
					return new JSONString(builder.toString());
			else if (c == '\\') {
				if (!(escaped ^= true))
					builder.append('\\');
			} else if (c == '\b' || c == '\f' || c == '\n' || c == '\r' || c == '\t')
				throw new IllegalArgumentException(
						"Malformed JSON. Unescaped control character found in string literal.");
			else {
				if (c == -1)
					throw new IllegalArgumentException("Malformed JSON. End of input found before string termination.");
				if (escaped) {
					switch (c) {
					case 'b':
						builder.append('\b');
						break;
					case 'f':
						builder.append('\f');
						break;
					case 'n':
						builder.append('\n');
						break;
					case 'r':
						builder.append('\r');
						break;
					case 't':
						builder.append('\t');
						break;
					case 'u':
						StringBuilder n = new StringBuilder();
						for (int nc = stream.next(), i = 0; i < 4; i++) {
							if (nc == -1)
								throw new IllegalArgumentException(
										"Malformed JSON. End of input found before string termination.");
							if (!isHexDigit((char) nc))
								throw new IllegalArgumentException("Malformed JSON.");
							n.append((char) nc);
						}

						builder.append((char) Integer.parseUnsignedInt(n.toString(), 16));

						break;
					default:
						throw new IllegalArgumentException("Malformed JSON.");
					}
					escaped = false;
				} else
					builder.append((char) c);
			}
		}
	}

	/**
	 * Parses the portion of an object immediately following the opening brace
	 * (<code>{</code>).
	 * 
	 * @param stream The stream whose {@link CharacterStream#next() next character}
	 *               should be the character immediately following the opening
	 *               brace.
	 * @return The parsed {@link JSONObject}.
	 * @precon <font color=green>The stream's next character is the character
	 *         immediately following the opening brace of the {@link JSONObject} to
	 *         be parsed.</font>
	 * @postcon <font color=red>The stream's next character is the character
	 *          immediately following the last character of the parsed
	 *          {@link JSONObject}.</font>
	 */
	private JSONObject parseHeadlessObject(CharacterSequence stream) {
		JSONObject obj = new JSONObject();

		int c = parseWhitespace(stream);
		if (c == '}')
			return obj;
		else {
			// If a closing brace isn't found, a string is expected.
			if (c != '"')
				throw new IllegalArgumentException("Malformed JSON.");
			JSONString str = parseHeadlessString(stream);
			c = parseWhitespace(stream);
			if (c == -1)
				throw new IllegalArgumentException("Malformed JSON. End of input reached while parsing object.");
			else if (c != ':')
				throw new IllegalArgumentException("The key string in an object was not followed by a ':'.");
			else
				obj.put(str.getValue(), parseValue(stream.next(), stream));
			while ((c = stream.next()) == ',') {
				c = parseWhitespace(stream);
				if (c != '"')
					throw new IllegalArgumentException("Malformed JSON.");
				c = parseWhitespace(stream);
				if (c == -1)
					throw new IllegalArgumentException("Malformed JSON. End of input reached while parsing object.");
				else if (c != ':')
					throw new IllegalArgumentException("The key string in an object was not followed by a ':'.");
				else
					obj.put(str.getValue(), parseValue(stream.next(), stream));
			}
			if (c == '}') {
				return obj;
			} else
				throw new IllegalArgumentException("Malformed JSON.");
		}
	}

	private JSONArray parseHeadlessArray(CharacterSequence stream) {
		JSONArray arr = new JSONArray();

		int c = parseWhitespace(stream);
		if (c != ']') {
			while (true) {
				arr.add(parseValue(c, stream));
				c = parseWhitespace(stream);
				if (c == ']')
					return arr;
				else if (c == ',') {
					c = parseWhitespace(stream);
					continue;
				} else
					throw new IllegalArgumentException(
							c == -1 ? "Malformed JSON. End of input reached while parsing array." : "Malformed JSON.");
			}
		} else
			return arr;

	}

	private JSONNumber parseNumber(int curr, CharacterSequence stream) {
		boolean neg = curr == '-';
		if (!neg)
			curr = stream.next();
		StringBuilder left = new StringBuilder();

		if (curr == -1)
			throw new IllegalArgumentException("Malformed JSON. End of input reached while parsing number.");
		else if (isDigit1_9((char) curr)) {
			left.append((char) curr);
			while (Character.isDigit(curr = stream.next()))
				left.append((char) curr);
		} else if (curr != '0')
			throw new IllegalArgumentException("Malformed JSON.");
		else {
			left.append('0');
			curr = stream.next();
		}

		StringBuilder right = new StringBuilder();
		if (curr == '.') {
			curr = stream.next();
			if (curr == -1)
				throw new IllegalArgumentException("Malformed JSON. End of input reached while parsing number.");
			else if (!Character.isDigit(curr))
				throw new IllegalArgumentException("Malformed JSON.");
			right.append((char) curr);
			while (Character.isDigit(curr = stream.next()))
				right.append((char) curr);
		}

		StringBuilder exp = new StringBuilder();
		if (curr == 'e' || curr == 'E') {
			curr = stream.next();
			if (curr == -1)
				throw new IllegalArgumentException("Malformed JSON. End of input reached while parsing number.");
			else if (curr == '-' || curr == '+')
				exp.append((char) curr);
			else if (!Character.isDigit(curr))
				throw new IllegalArgumentException("Malformed JSON.");
			else
				exp.append((char) curr);
			while (Character.isDigit(curr = stream.next()))
				right.append((char) curr);
		}
		stream.back();

		return new JSONNumber(neg, left.toString(), right.toString(), exp.toString());
	}

	/**
	 * Parses a value, where the first character in the value is specified by the
	 * <code>curr</code> argument, and the rest of the value (as well as the rest of
	 * the JSON input) is specified in the <code>stream</code> argument. Upon
	 * successful completion, this method leaves off such that a subsequent call to
	 * {@link CharacterStream#next() next} on the specified <code>stream</code>
	 * argument will return the character immediately following the parsed
	 * {@link JSONValue}.
	 * 
	 * @param curr   The current character that the stream last returned.
	 * @param stream The stream.
	 * @return The parsed {@link JSONValue}.
	 * @precon <font color=green><code>curr</code> is the first character that is a
	 *         part of the {@link JSONValue} to be parsed, or is whitespace
	 *         preceding that character. The stream's next character will be the
	 *         character immediately following <code>curr</code>.</font>
	 * @postcon <font color=red>The next character in the stream will be the
	 *          character immediately after the parsed {@link JSONValue}.</font>
	 */
	private JSONValue parseValue(int curr, CharacterSequence stream) {
		if (Character.isWhitespace(curr))
			curr = parseWhitespace(stream);
		if (curr == '{')
			return parseHeadlessObject(stream);
		else if (curr == '[')
			return parseHeadlessArray(stream);
		else if (curr == '"')
			return parseHeadlessString(stream);
		else if (curr == '-' || Character.isDigit(curr))
			return parseNumber(curr, stream);
		else if (curr == 't')
			if (stream.next() != 'r' || stream.next() != 'u' || stream.next() != 'e')
				throw new IllegalArgumentException("Malformed JSON.");
			else
				return JSONConstant.TRUE;
		else if (curr == 'f')
			if (stream.next() != 'a' || stream.next() != 'l' || stream.next() != 's' || stream.next() != 'e')
				throw new IllegalArgumentException("Malformed JSON.");
			else
				return JSONConstant.FALSE;
		else if (curr == 'n')
			if (stream.next() != 'u' || stream.next() != 'l' || stream.next() != 'l')
				throw new IllegalArgumentException("Malformed JSON.");
			else
				return JSONConstant.NULL;
		else if (curr == -1)
			throw new IllegalArgumentException("End of JSON encountered while parsing value.");
		else
			throw new IllegalArgumentException("Malformed JSON.");
	}

	private JSONValue parseElement(CharacterStream stream) {
		int c = parseWhitespace(stream);
		return parseValue(c, new CharacterSequence(stream));
	}

	public JSONValue parse(CharacterStream stream) {
		return parseElement(stream);
	}

	private class CharacterSequence implements CharacterStream {
		private final CharacterStream backing;

		public CharacterSequence(CharacterStream backing) {
			this.backing = backing;
		}

		private int curr;
		private boolean backed;

		@Override
		public int next() {
			if (backed) {
				backed = false;
				return curr;
			} else
				return curr = backing.next();
		}

		public void back() {
			backed = true;
		}
	}
}
