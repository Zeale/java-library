package org.alixia.javalibrary.javafx;

import javafx.scene.text.Font;

public final class JavaFXTools {
	private JavaFXTools() {
	}

	public interface PotentialFont {
		/**
		 * Returns the best {@link Font} that matches this {@link PotentialFont}, or
		 * <code>null</code> if no matching {@link Font} could be found.
		 * 
		 * @return The {@link Font}, if found, or <code>null</code>.
		 */
		Font pick();

		/**
		 * Returns the result of a call to {@link #pick()} on this object if the result
		 * is not <code>null</code>. Otherwise, this method runs and returns
		 * {@link #pick()} on the specified {@link PotentialFont}.
		 * 
		 * @param other The fallback {@link PotentialFont}.
		 * @return The {@link Font} if found, otherwise <code>null</code>.
		 */
		default Font or(PotentialFont other) {
			Font font = pick();
			return font == null ? other.pick() : font;
		}
	}
}
