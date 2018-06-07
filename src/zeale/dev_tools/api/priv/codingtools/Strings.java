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

	private Strings() {
	}

	public static String reverse(String string) {
		return new StringBuilder(string).reverse().toString();
	}

	public static String invertCase(String string) {
		String newString = "";
		for (char c : string.toCharArray())
			newString += Character.isLowerCase(c) ? Character.toUpperCase(c) : Character.toLowerCase(c);
		return newString;
	}

	public static String sortChars(String text) {
		String newString = "";
		List<Character> chars = new ArrayList<>();
		for (char c : text.toCharArray())
			chars.add(c);
		chars.sort(null);
		for (char c : chars)
			newString += c;
		return newString;
	}

	public static String countChars(String text) {
		Map<Character, Integer> occurrences = new HashMap<>();
		for (char c : text.toCharArray())
			occurrences.put(c, occurrences.containsKey(c) ? occurrences.get(c) + 1 : 1);

		String result = "";
		for (Entry<Character, Integer> e : occurrences.entrySet()) {
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

	public static String toNumbers(String text, boolean spaces) {
		String result = "";
		for (char c : text.toCharArray()) {
			result += (int) c;
			if (spaces)
				result += " ";
		}

		return result.trim();// Delete lagging space.

	}

	public static enum TextType {

		NUMBERS("1234567890"), BASIC_UPPER_CASE_LETTERS("ABCDEFGHIJKLMNOPQRSTUVWXYZ"), BASIC_LOWER_CASE_LETTERS(
				"abcdefghijklmnopqrstuvwxyz"), PUNCTUATION(
						",./;'-!()?:\""), MATH_CHARS("=+-/*()^%<>"), OTHER_BASIC_CHARS("{}[]\\|_&$#@~`");

		private final String[] texts;

		TextType(String... texts) {
			for (int i = 0; i < texts.length; i++) {
				if (texts[i] == null)
					texts[i] = "";
			}
			this.texts = texts;
		}

		TextType(String chars) {
			texts = new String[chars.length()];
			char[] charArray = chars.toCharArray();
			for (int i = 0; i < charArray.length; i++)
				texts[i] = "" + charArray[i];
		}

		@Override
		public String toString() {
			String[] pieces = name().split("_");
			String result = "";
			for (String s : pieces)
				result += s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase() + " ";
			return result.trim();
		}

	}

	public static String generateRandomText(int count, boolean useSpaces, TextType... types) {
		String result = "";

		for (int i = 0; i < count; i++) {
			String[] texts = types[(int) (Math.random() * types.length)].texts;
			result += texts[(int) (Math.random() * texts.length)];
			if (useSpaces)
				result += " ";
		}

		return result.trim();
	}

}
