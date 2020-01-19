package org.alixia.javalibrary.json;

public enum JSONConstant implements JSONValue {
	TRUE, FALSE, NULL;

	@Override
	public String toString() {
		return name().toLowerCase();
	}

	@Override
	public String toString(String indentation) {
		return toString();
	}
}
