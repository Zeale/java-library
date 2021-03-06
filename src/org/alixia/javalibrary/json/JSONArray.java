package org.alixia.javalibrary.json;

import java.util.ArrayList;
import java.util.Iterator;

import org.alixia.javalibrary.JavaTools;

public class JSONArray extends ArrayList<JSONValue> implements JSONValue {

	public JSONArray(JSONValue... values) {
		for (JSONValue jv : values)
			add(jv);
	}

	public JSONArray(Iterable<? extends JSONValue> values) {
		for (JSONValue jv : values)
			add(jv);
	}

	public JSONArray(Iterator<? extends JSONValue> values) {
		this(JavaTools.iterable(values));
	}

	/**
	 * SUID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString(String indentation) {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		if (isEmpty())
			builder.append('\t');
		else {
			Iterator<JSONValue> iterator = iterator();
			builder.append('\n').append(indentation).append('\t').append(iterator.next().toString(indentation + '\t'));
			for (; iterator.hasNext();) {
				JSONValue v = iterator.next();
				builder.append(",\n").append(indentation).append('\t').append(v.toString(indentation + '\t'));
			}
			builder.append('\n');
		}
		builder.append(indentation).append(']');

		return builder.toString();
	}

	@Override
	public String toString() {
		return toString("");
	}

}
