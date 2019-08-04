package org.alixia.javalibrary.strings.matching;

import org.alixia.javalibrary.streams.CharacterStream;

/**
 * @author Zeale
 */
public interface Matching {

	/**
	 * <p>
	 * Returns the text that remains after the given {@link String} is matched
	 * against this object. If the text does not match this object, the full text is
	 * returned. This method always returns a {@link String} object.
	 * </p>
	 * <p>
	 * For example, consider the {@link Matching}:
	 * 
	 * <pre>
	 * <code>Matching m = Matching.build("p").then(Matching.ignoreCase("q"));</code>
	 * </pre>
	 * 
	 * Following is the result of the {@link Matching} functions,
	 * {@link #match(String)}, {@link #matches(String)}, and
	 * {@link #fullyMatches(String)}, called on <code>m</code>, against various
	 * {@link String}s.
	 * 
	 * <style> table, th, td { border: 1px solid black; border-collapse: collapse; }
	 * td { padding: 3px; } th { padding: 5px; } </style> <br>
	 * <br>
	 * <table style="width:100%;" border=1>
	 * <tr>
	 * <th>Input</th>
	 * <th>{@link #match(String)}</th>
	 * <th>{@link #matches(String)}</th>
	 * <th>{@link #fullyMatches(String)}</th>
	 * </tr>
	 * <tr>
	 * <td><code>"p"</code></td>
	 * <td><code>"p"</code></td>
	 * <td><code>false</code></td>
	 * <td><code>false</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>"P"</code></td>
	 * <td><code>"P"</code></td>
	 * <td><code>false</code></td>
	 * <td><code>false</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>"pX"</code></td>
	 * <td><code>"pX"</code></td>
	 * <td><code>false</code></td>
	 * <td><code>false</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>"pq"</code></td>
	 * <td><code>""</code></td>
	 * <td><code>true</code></td>
	 * <td><code>true</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>"pqX"</code></td>
	 * <td><code>"X"</code></td>
	 * <td><code>true</code></td>
	 * <td><code>false</code></td>
	 * </tr>
	 * </table>
	 * <br>
	 * 
	 * @param text The {@link String} to match this object against.
	 * @return The remaining text that didn't match this object.
	 */
	String match(String text);

	/**
	 * Returns <code>true</code> if the given {@link String} starts with this
	 * {@link Matching}. Every necessary part of the matching must be fulfilled in
	 * the given {@link String} for this method to return <code>true</code>, but
	 * there can be excessive characters after
	 * 
	 * @param text The text to match against this object.
	 * @return <code>text.length() != this.match().length()</code>
	 */
	default boolean matches(String text) {
		return text.length() != match(text).length();
	}

	/**
	 * Matches, exactly, against the given text.
	 * 
	 * @param text The text to match against.
	 * @return <code>true</code> if the given text is an exact match,
	 *         <code>false</code> otherwise.
	 */
	default boolean fullyMatches(String text) {
		return match(text).isEmpty();
	}

	static Matching build(String matching) {
		return text -> text.startsWith(matching) ? text.substring(matching.length()) : text;
	}

	static Matching build(String... matchings) {
		return text -> {
			for (String s : matchings)
				if (text.startsWith(s))
					return text.substring(s.length());
			return text;
		};
	}

	static Matching whitespace() {
		return text -> {
			if (text.isEmpty() || !Character.isWhitespace(text.charAt(0)))
				return text;

			CharacterStream strm = CharacterStream.from(text);
			for (int c = strm.next(); c != -1; c = strm.next())
				if (!Character.isWhitespace(c)) {
					StringBuilder buff = new StringBuilder();
					buff.append((char) c);
					while ((c = strm.next()) != -1)
						buff.append((char) c);
					return buff.toString();
				}
			return "";
		};
	}

	static Matching identifier() {
		return text -> {
			if (text.isEmpty() || !(Character.isLetter(text.charAt(0)) || text.charAt(0) == '_'))
				return text;
			while (!text.isEmpty() && (Character.isLetter(text.charAt(0)) || Character.isDigit(text.charAt(0))
					|| text.charAt(0) == '_'))
				text = text.substring(1);
			return text;
		};
	}

	static Matching ignoreCase(String... matchings) {
		return text -> {
			String text2 = text.toLowerCase();
			for (String s : matchings)
				if (text2.startsWith(s.toLowerCase()))
					return text.substring(s.length());
			return text;
		};
	}

	static Matching ignoreCase(String matching) {
		return text -> text.toLowerCase().startsWith(matching.toLowerCase()) ? text.substring(matching.length()) : text;
	}

	default Matching or(String... others) {
		return or(false, others);
	}

	default Matching then(String... others) {
		return then(false, others);
	}

	default Matching possibly(String... others) {
		return possibly(false, others);
	}

	default Matching or(Matching other) {
		return text -> {
			String firstMatch = match(text);
			return firstMatch.length() == text.length() ? other.match(text) : firstMatch;
		};
	}

	default Matching then(Matching other) {
		return text -> {
			String firstMatch = match(text);
			if (firstMatch.isEmpty() || firstMatch.length() == text.length())
				return text;
			String secondMatch = other.match(firstMatch);
			return secondMatch.length() == firstMatch.length() ? text : secondMatch;
		};
	}

	default Matching possibly(Matching other) {
		return text -> {
			String firstMatch = match(text);
			if (firstMatch.length() == text.length())// The previous text is required.
				return text;

			if (firstMatch.isEmpty())
				return "";
			String otherMatch = other.match(firstMatch);
			return otherMatch.length() == firstMatch.length() ? firstMatch : otherMatch;
		};
	}

	default Matching or(boolean ignoreCase, String... others) {
		return or(ignoreCase ? ignoreCase(others) : build(others));
	}

	default Matching then(boolean ignoreCase, String... others) {
		return then(ignoreCase ? ignoreCase(others) : build(others));
	}

	default Matching possibly(boolean ignoreCase, String... others) {
		return possibly(ignoreCase ? ignoreCase(others) : build(others));
	}

	/**
	 * Returns a matching which optionally matches <code>thiz</code>, but
	 * necessarily matches <code>then</code>.
	 * 
	 * @param thiz The "possible" matching.
	 * @param then The following, required matching.
	 * @return A compound {@link Matching} built off of the two parameters.
	 */
	static Matching possibly(Matching thiz, Matching then) {
		return text -> {
			// This method can encounter "problems" when thiz and then are the same. Docs
			// are needed.

			String thisMatch = thiz.match(text);
			String furtherMatch = then.match(thisMatch);

			if (furtherMatch.length() != thisMatch.length()) {
				// If then.match() consumed something, we don't care if thiz.match() did,
				// because thiz is optional.
				return furtherMatch;
			} else if (thisMatch.length() != text.length()) {
				// If then wasn't matched however, we'll need to see if it can be matched when
				// thiz isn't matched in the first place. We don't want our optional thiz
				// element consuming text that the necessary then element would otherwise
				// consume.
				//
				// This situation only applies when thisMatch.length() != text.length().
				return then.match(text);
			}
			return text;
		};
	}

}
