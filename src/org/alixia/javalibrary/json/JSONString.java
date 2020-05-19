package org.alixia.javalibrary.json;

import java.util.Collection;

public class JSONString implements JSONValue {
	/**
	 * SUID
	 */
	private static final long serialVersionUID = 1L;
	private final String value;

	public JSONString(String value) {
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
