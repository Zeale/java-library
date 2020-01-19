package org.alixia.javalibrary.json;

import java.util.HashMap;

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
		else
			for (Entry<String, JSONValue> e : entrySet())
				builder.append('\n').append(indentation).append('\t').append('"').append(e.getKey()).append('"')
						.append(':').append(e.getValue().toString(indentation + '\t'));
		builder.append('}');

		return builder.toString();
	}

}
