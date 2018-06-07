package org.alixia.chatroom.api.printables;

import javafx.scene.paint.Color;

public interface ColoredPrintable extends Printable {
	void print(String text, Color color);

	@Override
	default void print(String text) {
		print(text, Color.WHITE);
	}

	default void println(final String text, final Color color) {
		print(text, color);
		println();
	}
}
