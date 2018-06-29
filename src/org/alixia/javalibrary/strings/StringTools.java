package org.alixia.javalibrary.strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.alixia.chatroom.api.QuickList;

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

		Map<String, Integer> matchingPieces = new HashMap<>(0);
		List<String> standbies = new LinkedList<>(), mute = new ArrayList<>();
		for (String s : possiblePieces)
			if (s == null || s.isEmpty())
				continue;
			else
				standbies.add(s.toLowerCase());

		for (char c : string.toCharArray()) {
			for (Iterator<Entry<String, Integer>> iterator = matchingPieces.entrySet().iterator(); iterator
					.hasNext();) {
				Entry<String, Integer> e = iterator.next();
				char nextChar = e.getKey().charAt(e.getValue());
				if (nextChar == c) {
					e.setValue(e.getValue() + 1);
					if (e.getValue() == e.getKey().length())
						return true;
				} else
					iterator.remove();
			}
			for (String s : standbies) {
				char firstChar = s.charAt(0);
				if (firstChar == c)
					if (s.length() == 1)
						return true;
					else
						matchingPieces.put(s, 1);
			}
		}

		return false;
	}

	public static void main(String[] args) {
		// TODO We had an issue...
		System.out.println(containsIgnoreCase("ststrong", "strong"));// false
		// I fixed it doe. :)
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
