package org.alixia.javalibrary.json;

import java.util.HashMap;
import java.util.Iterator;

public class JSONObject extends HashMap<String, JSONValue> implements JSONValue {

	/**
	 * SUID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return toString("");
	}

	@Override
	public String toString(String indentation) {
		StringBuilder builder = new StringBuilder();
		builder.append('{');
		if (isEmpty())
			builder.append('\t');
		else {
			Iterator<Entry<String, JSONValue>> iterator = entrySet().iterator();
			Entry<String, JSONValue> e = iterator.next();
			builder.append('\n').append(indentation).append('\t').append('"').append(JSONValue.escape(e.getKey())).append('"').append(':')
					.append(e.getValue().toString(indentation + '\t'));
			for (; iterator.hasNext();) {
				e = iterator.next();
				builder.append(",\n").append(indentation).append('\t').append('"').append(e.getKey()).append('"')
						.append(':').append(e.getValue().toString(indentation + '\t'));
			}
			builder.append('\n');
		}
		builder.append(indentation).append('}');

		return builder.toString();
	}

}
