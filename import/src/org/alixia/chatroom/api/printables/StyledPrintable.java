package org.alixia.chatroom.api.printables;

import javafx.scene.paint.Color;

public interface StyledPrintable extends ColoredPrintable {

	@Override
	default void print(final String text, final Color color) {
		print(text, color, false, false);
	}

	void print(String text, Color color, boolean bold, boolean italicized);

}
