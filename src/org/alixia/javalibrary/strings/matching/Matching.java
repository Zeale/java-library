package org.alixia.javalibrary.strings.matching;

/**
 * @author Zeale
 */
public interface Matching {
	/**
	 * Matches the given <code>text</code> against this object. Returns the
	 * remaining text, the text that didn't match this object.
	 * 
	 * @param text The {@link String} to match this object against.
	 * @return The remaining text that didn't match this object.
	 */
	String match(String text);

	/**
	 * Returns <code>true</code> if the text matched this string. The actual
	 * implementation of this method actually returns <code>true</code> if the
	 * returned text from {@link #match(String)} is the same as the input text to
	 * this method. Since {@link #match(String)} removes whatever matches, and
	 * returns the rest, if the input is the same as what it returns, nothing should
	 * have matched.
	 * 
	 * @param text The text to match against this object.
	 * @return <code>!text.equals(this.match(text))</code>
	 */
	default boolean matches(String text) {
		return !text.equals(match(text));
	}

	static Matching build(String matching) {
		return text -> text.startsWith(matching) ? text.substring(matching.length()) : null;
	}

	static Matching build(String... matchings) {
		return text -> {
			for (String s : matchings)
				if (text.startsWith(s))
					return text.substring(s.length());
			return null;
		};
	}

	static Matching whitespace() {
		return text -> {
			if (text.isEmpty() || !Character.isWhitespace(text.charAt(0)))
				return null;
			while (!text.isEmpty() && Character.isWhitespace(text.charAt(0)))
				text = text.substring(1);
			return text;
		};
	}

	static Matching identifier() {
		return text -> {
			if (text.isEmpty() || !(Character.isLetter(text.charAt(0)) || Character.isDigit(text.charAt(0))
					|| text.charAt(0) == '_'))
				return null;
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
			return null;
		};
	}

	static Matching ignoreCase(String matching) {
		return text -> text.toLowerCase().startsWith(matching.toLowerCase()) ? text.substring(matching.length()) : null;
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
			return firstMatch == null ? other.match(text) : firstMatch;
		};
	}

	default Matching then(Matching other) {
		return text -> {
			String firstMatch = match(text);
			return (firstMatch != null) ? other.match(firstMatch) : null;
		};
	}

	default Matching possibly(Matching other) {
		return text -> {
			String firstMatch = match(text);
			if (firstMatch == null)
				return null;
			String otherMatch = other.match(firstMatch);
			return otherMatch == null ? firstMatch : otherMatch;
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

	static Matching possibly(Matching thiz, Matching then) {
		return text -> {
			String thisMatch = thiz.match(text);
			return then.match(thisMatch != null ? thisMatch : text);
		};
	}

}
