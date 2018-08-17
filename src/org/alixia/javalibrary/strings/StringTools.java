package org.alixia.javalibrary.strings;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class StringTools {
	/**
	 * <p>
	 * Returns true if any of <code>possiblePieces</code> are contained inside the
	 * given String, <code>string</code>.
	 * </p>
	 * <p>
	 * Note: This method tends to be faster than comparing possible matches and the
	 * base string with {@link String#contains(CharSequence)}, after calling
	 * {@link String#toLowerCase()} upon all of them, if the base string contains
	 * fragments of the possible pieces, (where the fragments start with the same
	 * characters as their respective pieces), or if any of the possible pieces are
	 * near a long list of possible pieces, in comparison to a for loop. The latter
	 * is due to the fact that this method checks against all the pieces
	 * simultaneously, so if a match is near the end of <code>possiblePieces</code>,
	 * it doesn't have to wait for all the other possible pieces to be checked
	 * before it can be checked.
	 * </p>
	 * 
	 * @param string
	 *            The string that may contain something in
	 *            <code>possiblePieces</code>.
	 * @param possiblePieces
	 *            Any number of Strings. If <code>string</code> contains one of
	 *            these, this method will return true.
	 * @return <code>true</code> if one of the <code>possiblePieces</code> are found
	 *         inside <code>string</code>. False otherwise.
	 */
	public static boolean containsIgnoreCase(String string, String... possiblePieces) {

		string = string.toLowerCase();

		List<String> standbies = new LinkedList<>();
		List<Pair> matchingPieces = new LinkedList<>();
		for (String s : possiblePieces)
			if (s == null || s.isEmpty())
				continue;
			else
				standbies.add(s);

		char[] charArray = string.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			for (Iterator<Pair> iterator = matchingPieces.iterator(); iterator.hasNext();) {
				Pair e = iterator.next();
				char nextChar = Character.toLowerCase(e.string.charAt(e.value));
				if (nextChar == c) {
					e.value++;
					if (e.value == e.getLength())
						return true;
				} else
					iterator.remove();
			}
			for (Iterator<String> iterator = standbies.iterator(); iterator.hasNext();) {
				String s = iterator.next();
				if (s.length() > charArray.length - i)
					iterator.remove();
				else {
					char firstChar = Character.toLowerCase(s.charAt(0));
					if (firstChar == c)
						if (s.length() == 1)
							return true;
						else
							matchingPieces.add(new Pair(s, 1));
				}
			}
		}

		return false;
	}

	public static boolean equalsAny(final String arg, final String... possibleMatches) {
		if (arg == null)
			return false;
		for (final String s : possibleMatches)
			if (arg.equals(s))
				return true;
		return false;
	}

	public static boolean equalsAnyIgnoreCase(final String arg, final String... possibleMatches) {
		if (arg == null)
			return false;
		for (final String s : possibleMatches)
			if (arg.equalsIgnoreCase(s))
				return true;
		return false;
	}

}
