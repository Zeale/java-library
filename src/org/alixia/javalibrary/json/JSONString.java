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
}
