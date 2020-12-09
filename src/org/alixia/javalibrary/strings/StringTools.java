package org.alixia.javalibrary.strings;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
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

	private static final byte[] HEX_CHAR_BYTES = "0123456789ABCDEF".getBytes(StandardCharsets.UTF_8);

	public static String toHexString(byte... b) {
		byte[] rb = new byte[b.length * 2];

		for (int i = 0; i < b.length; i++) {
			rb[i * 2] = HEX_CHAR_BYTES[(b[i] >>> 4) & 0xF];
			rb[i * 2 + 1] = HEX_CHAR_BYTES[b[i] & 0xF];
		}

		return new String(rb, StandardCharsets.UTF_8);
	}

	public static byte[] fromHexString(String b) {
		if ((b.length() & 1) != 0)
			throw new NumberFormatException("Input must be pairs of hex characters.");
		byte[] rb = new byte[b.length() / 2];
		for (int i = 0; i < b.length(); i += 2)
			rb[i / 2] = (byte) (hexVal(b.charAt(i)) << 4 | hexVal(b.charAt(i + 1)));
		return rb;
	}

	private static byte hexVal(char c) {
		switch (c) {
		case '0':
			return 0;
		case '1':
			return 1;
		case '2':
			return 2;
		case '3':
			return 3;
		case '4':
			return 4;
		case '5':
			return 5;
		case '6':
			return 6;
		case '7':
			return 7;
		case '8':
			return 8;
		case '9':
			return 9;
		case 'A':
			return 10;
		case 'B':
			return 11;
		case 'C':
			return 12;
		case 'D':
			return 13;
		case 'E':
			return 14;
		case 'F':
			return 15;
		default:
			throw new NumberFormatException("Input must be hex chars.");
		}
	}

	public static String toBase64String(byte... data) {
		return Base64.getEncoder().encodeToString(data);
	}

	public static byte[] fromBase64String(String base64) {
		return Base64.getDecoder().decode(base64);
	}

	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++)
			if (!Character.isDigit(str.charAt(i)))
				return false;
		return true;
	}

}
