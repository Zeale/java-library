package org.alixia.javalibrary.strings.matching;

/**
 * @author Zeale
 */
public class ManipulableString {
	private String text;

	public ManipulableString(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String consumeIf(String... matches) {
		for (String s : matches)
			if (text.startsWith(s)) {
				return text = text.substring(s.length());
			}
		return null;
	}

	/**
	 * @param matches The matchings to match this against.
	 * @return The remaining text (i.e., the same as the result of an immediate call
	 *         to {@link #getText()}), or <code>null</code> if nothing was matched.
	 */
	public String consumeIf(Matching... matches) {
		String match;
		for (Matching m : matches)
			if ((match = m.match(text)) != null)
				return text = match;
		return null;
	}

	public String consumeIfIgnoreCase(String... matches) {
		for (String s : matches)
			if (text.toLowerCase().startsWith(s.toLowerCase()))
				return text = text.substring(s.length());
		return null;
	}

	@Override
	public String toString() {
		return text;
	}

}