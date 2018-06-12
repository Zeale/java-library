package zeale.dev_tools.api.priv.codingtools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This is basically a helper class for {@link StringManipulationTool}.
 *
 * @author Zeale
 *
 */
public final class Strings {

	public static enum TextType {

		NUMBERS("1234567890"), BASIC_UPPER_CASE_LETTERS("ABCDEFGHIJKLMNOPQRSTUVWXYZ"), BASIC_LOWER_CASE_LETTERS(
				"abcdefghijklmnopqrstuvwxyz"), PUNCTUATION(
						",./;'-!()?:\""), MATH_CHARS("=+-/*()^%<>"), OTHER_BASIC_CHARS("{}[]\\|_&$#@~`");

		private final String[] texts;

		TextType(final String... texts) {
			for (int i = 0; i < texts.length; i++)
				if (texts[i] == null)
					texts[i] = "";
			this.texts = texts;
		}

		TextType(final String chars) {
			texts = new String[chars.length()];
			final char[] charArray = chars.toCharArray();
			for (int i = 0; i < charArray.length; i++)
				texts[i] = "" + charArray[i];
		}

		@Override
		public String toString() {
			final String[] pieces = name().split("_");
			String result = "";
			for (final String s : pieces)
				result += s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase() + " ";
			return result.trim();
		}

	}

	public static String countChars(final String text) {
		final Map<Character, Integer> occurrences = new HashMap<>();
		for (final char c : text.toCharArray())
			occurrences.put(c, occurrences.containsKey(c) ? occurrences.get(c) + 1 : 1);

		String result = "";
		for (final Entry<Character, Integer> e : occurrences.entrySet()) {
			if (e.getKey() == ':' || Character.isWhitespace(e.getKey()))
				result += "\"" + e.getKey() + "\"";
			else if (e.getKey() == '"')
				result += "'\"'";
			else
				result += e.getKey();
			result += ":" + e.getValue() + " ";
		}

		return result.trim();// .trim() deletes lagging space.
	}

	public static String generateRandomText(final int count, final boolean useSpaces, final TextType... types) {
		String result = "";

		for (int i = 0; i < count; i++) {
			final String[] texts = types[(int) (Math.random() * types.length)].texts;
			result += texts[(int) (Math.random() * texts.length)];
			if (useSpaces)
				result += " ";
		}

		return result.trim();
	}

	public static String invertCase(final String string) {
		String newString = "";
		for (final char c : string.toCharArray())
			newString += Character.isLowerCase(c) ? Character.toUpperCase(c) : Character.toLowerCase(c);
		return newString;
	}

	public static String reverse(final String string) {
		return new StringBuilder(string).reverse().toString();
	}

	public static String sortChars(final String text) {
		String newString = "";
		final List<Character> chars = new ArrayList<>();
		for (final char c : text.toCharArray())
			chars.add(c);
		chars.sort(null);
		for (final char c : chars)
			newString += c;
		return newString;
	}

	public static String toNumbers(final String text, final boolean spaces) {
		String result = "";
		for (final char c : text.toCharArray()) {
			result += (int) c;
			if (spaces)
				result += " ";
		}

		return result.trim();// Delete lagging space.

	}

	private Strings() {
	}

}
