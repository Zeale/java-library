package org.alixia.javalibrary.json;

import java.util.Collection;

import org.alixia.javalibrary.JavaTools;

public class JSONString implements JSONValue {
	/**
	 * SUID
	 */
	private static final long serialVersionUID = 1L;
	private final String value;

	/**
	 * Constructs a {@link JSONString} that holds the provided {@link String}. The
	 * provided {@link String} may not be <code>null</code>.
	 * 
	 * @param value The {@link String} that this {@link JSONString} should hold.
	 * @author Gartham
	 */
	public JSONString(String value) {
		JavaTools.requireNonNull(value);
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return '"' + JSONValue.escape(value) + '"';
	}

	/**
	 * Adds the value of each {@link JSONString} in the {@link JSONString}
	 * collection to the other collection.
	 * 
	 * @param <T>
	 * @param c
	 * @param t
	 */
	public static <T extends Collection<? super String>> void addAll(Collection<? extends JSONString> c, T t) {
		for (JSONString j : c)
			t.add(j.getValue());
	}

	@Override
	public String toString(String indentation) {
		return toString();
	}
}
