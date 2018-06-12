package org.alixia.chatroom.api.printables;

public interface Printable {
	void print(String text);

	default void println() {
		print("\n");
	}

	default void println(final String line) {
		print(line);
		println();
	}
}
