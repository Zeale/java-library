package org.alixia.chatroom.api.printables;

import javafx.scene.paint.Color;

public interface ColoredPrintable extends Printable {
	@Override
	default void print(final String text) {
		print(text, Color.WHITE);
	}

	void print(String text, Color color);

	default void println(final String text, final Color color) {
		print(text, color);
		println();
	}
}
