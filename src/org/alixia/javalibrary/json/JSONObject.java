package org.alixia.javalibrary.json;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JSONObject extends HashMap<String, JSONValue> implements JSONValue {

	public JSONObject(Map<? extends String, ? extends JSONValue> map) {
		for (Entry<? extends String, ? extends JSONValue> e : map.entrySet())
			put(e.getKey(), e.getValue());
	}

	public JSONObject() {
	}

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
			builder.append('\n').append(indentation).append('\t').append('"').append(JSONValue.escape(e.getKey()))
					.append('"').append(':').append(e.getValue().toString(indentation + '\t'));
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

	public JSONString getJString(String key) {
		return (JSONString) get(key);
	}

	public String getString(String key) {
		return getJString(key).getValue();
	}

	public JSONConstant getJConstant(String key) {
		return (JSONConstant) get(key);
	}

	public boolean getBoolean(String key) {
		switch (getJConstant(key)) {
		case FALSE:
			return false;
		case TRUE:
			return true;
		default:
			throw new ClassCastException("The JSON Constant stored for the key, " + key
					+ ", is not a boolean, but an attempt was made to access it as one.");
		}
	}

	public JSONNumber getJNumber(String key) {
		return (JSONNumber) get(key);
	}

	public int getInt(String key) {
		return getJNumber(key).intValue();
	}

	public long getLong(String key) {
		return getJNumber(key).longValue();
	}

	public JSONObject put(String key, String value) {
		put(key, new JSONString(value));
		return this;
	}

	public JSONObject put(String key, int value) {
		put(key, new JSONNumber(value));
		return this;
	}

	public JSONObject put(String key, long value) {
		put(key, new JSONNumber(value));
		return this;
	}

	public JSONObject putIfNonNull(String key, String value) {
		if (value != null)
			put(key, value);
		return this;
	}

	public JSONObject put(String key, boolean value) {
		put(key, value ? JSONConstant.TRUE : JSONConstant.FALSE);
		return this;
	}

	/**
	 * Returns a shallow copy of this {@link JSONObject}.
	 */
	@Override
	public JSONObject clone() {
		return new JSONObject(this);
	}

}
