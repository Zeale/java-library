package org.alixia.javalibrary.json;

public class JSONString implements JSONValue {
	private final String value;

	public JSONString(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
