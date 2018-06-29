package org.alixia.javalibrary.strings;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class StringTools {
	/**
	 * Returns true if any of <code>possiblePieces</code> are contained inside the
	 * given String, <code>string</code>.
	 * 
	 * @param string         The string that may contain something in
	 *                       <code>possiblePieces</code>.
	 * @param possiblePieces Any number of Strings. If <code>string</code> contains
	 *                       one of these, this method will return true.
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
		for (final String s : possibleMatches)
			if (arg.equals(s))
				return true;
		return false;
	}

	public static boolean equalsAnyIgnoreCase(final String arg, final String... possibleMatches) {
		for (final String s : possibleMatches)
			if (arg.equalsIgnoreCase(s))
				return true;
		return false;
	}

}
