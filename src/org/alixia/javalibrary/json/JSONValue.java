package org.alixia.javalibrary.json;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

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

	public static String toStringShort(JSONValue v) {
		if (v instanceof JSONArray) {
			StringBuilder sb = new StringBuilder("[");
			Iterator<JSONValue> iterator = ((JSONArray) v).iterator();
			if (iterator.hasNext()) {
				sb.append(toStringShort(iterator.next()));
				for (; iterator.hasNext();)
					sb.append(',').append(toStringShort(iterator.next()));
			}
			sb.append(']');
			return sb.toString();
		} else if (v instanceof JSONObject) {
			StringBuilder sb = new StringBuilder("{");
			Iterator<Entry<String, JSONValue>> iterator = ((JSONObject) v).entrySet().iterator();
			if (iterator.hasNext()) {
				Entry<String, JSONValue> item = iterator.next();
				sb.append('"').append(escape(item.getKey())).append("\":").append(toStringShort(item.getValue()));
				for (; iterator.hasNext();)
					sb.append(',').append('"').append(escape((item = iterator.next()).getKey())).append("\":")
							.append(toStringShort(item.getValue()));
			}
			sb.append('}');
			return sb.toString();
		} else
			return v.toString();
	}

	static String escape(String input) {
		return input.replace("\\", "\\\\").replace("\"", "\\\"")
				// .replace("/", "\\/")
				.replace("\b", "\\b").replace("\f", "\\f").replace("\r", "\\r").replace("\t", "\\t");
	}
}
