package org.alixia.javalibrary.strings;

public class Pair {
	public final String string;
	public int value;

	private int length = -1;

	public int getLength() {
		return length == -1 ? length = string.length() : length;
	}

	public Pair(String string, int value) {
		this.string = string;
		this.value = value;
	}
}