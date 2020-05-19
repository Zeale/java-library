package org.alixia.javalibrary.json;

import java.io.Serializable;
import java.util.Collection;

public interface JSONValue extends Serializable {
	String toString(String indentation);

	/**
	 * Adds the JSON {@link String} form of the objects in the given
	 * {@link JSONValue} {@link Collection} to the other collection.
	 * 
	 * @param <T>
	 * @param c
	 * @param t
	 */
	static <T extends Collection<? super String>> void addAll(Collection<? extends JSONValue> c, T t) {
		for (JSONValue j : c)
			t.add(j.toString());
	}

	static String escape(String input) {
		return input.replace("\\", "\\\\").replace("\"", "\\\"")
				// .replace("/", "\\/")
				.replace("\b", "\\b").replace("\f", "\\f").replace("\r", "\\r").replace("\t", "\\t");
	}
}
