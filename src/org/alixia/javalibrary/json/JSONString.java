package org.alixia.javalibrary.json;

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
		return '"' + value.replace("\\", "\\\\").replace("\"", "\\\"").replace("/", "\\/").replace("\b", "\\b")
				.replace("\f", "\\f").replace("\r", "\\r").replace("\t", "\\t") + '"';
	}

	@Override
	public String toString(String indentation) {
		return toString();
	}
}
