package org.alixia.chatroom.api.logging;

import org.alixia.chatroom.api.printables.StyledPrintable;

import javafx.scene.paint.Color;

public final class Logger {

	private static final Color DEFAULT_LOGGER_COLORS = Color.WHITE;

	private static StyledPrintable defaultPrinter = (text, color, bold, italicized) -> System.out.print(text);

	public static void setDefaultPrinter(final StyledPrintable printer) {
		defaultPrinter = printer;
	}

	public Color bracketColor = DEFAULT_LOGGER_COLORS, parentColor = DEFAULT_LOGGER_COLORS,
			childColor = DEFAULT_LOGGER_COLORS, separatorColor = DEFAULT_LOGGER_COLORS,
			messageColor = DEFAULT_LOGGER_COLORS, messageSeparatorColor = DEFAULT_LOGGER_COLORS;

	public boolean boldHeader = false;

	protected StyledPrintable printer = null;

	public void setPrinter(StyledPrintable printer) {
		this.printer = printer;
	}

	private final Logger parent;
	private final String name;

	public String separator = ".";

	public Logger(final String name) {
		this.name = name;
		parent = null;
	}

	public Logger(String name, Logger parent) {

		// Handle multiple loggers in one.
		if (name.contains(".")) {

			while (name.contains(".."))
				name = name.replace("..", ".");

			final String[] names = name.split("\\.");

			for (int i = 0; i < names.length - 1; i++) {
				final String n = names[i];
				parent = new Logger(n, parent);
			}

			this.name = names[names.length - 1];
		} else
			this.name = name;
		this.parent = parent;
	}

	public String getFullName() {
		return (parent == null ? "" : parent.getFullName() + ".") + name;
	}

	public String getName() {
		return name;
	}

	public Logger getParent() {
		return parent;
	}

	public String getSeparator() {
		return separator;
	}

	private StyledPrintable getPrinter() {
		return printer == null ? defaultPrinter : printer;
	}

	public void log(final String message) {
		printIdentifier();
		getPrinter().println(message, messageColor);
	}

	public void logBold(final String message) {
		printIdentifier();
		getPrinter().print(message + "\n", messageColor, true, false);
	}

	private void printIdentifier() {
		StyledPrintable printer = getPrinter();
		printer.print("[", bracketColor, boldHeader, false);
		final String[] names = getFullName().split("\\.");
		for (int i = 0; i < names.length - 1; i++) {
			final String s = names[i];
			printer.print(s, parentColor.interpolate(childColor, (double) i / (names.length - 1)), boldHeader, false);
			printer.print(separator, separatorColor, boldHeader, false);
		}
		printer.print(getName(), childColor, boldHeader, false);
		printer.print("]", bracketColor, boldHeader, false);
		printer.print(": ", messageSeparatorColor);
	}

	public void setSeparator(final String separator) {
		this.separator = separator;
	}

}
