package org.alixia.chatroom.api.commands;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class CommandManager {
	private final List<Command> commands = new LinkedList<>();

	private final Stack<CommandConsumer> consumers = new Stack<>();

	private String commandChar = "/";

	public CommandManager() {
	}

	public CommandManager(final String commandChar) {
		this.commandChar = commandChar;
	}

	public void addCommand(final Command command) {
		if (commands.contains(command))
			return;
		command.setManager(this);
		commands.add(command);
	}

	public String getCommandChar() {
		return commandChar;
	}

	public boolean hasConsumer() {
		return !consumers.isEmpty();
	}

	void pushConsumer(final CommandConsumer consumer) {
		// Stack can have dupes, in case consumption is wanted twice. Command list can't
		// have dupes.
		consumers.push(consumer);
	}

	public final boolean runCommand(final String rawInput) {

		final String input = rawInput.trim();
		if (input.isEmpty())
			return false;

		class Parser {
			int position;
			private final String text;

			public Parser(final String text) {
				this.text = text;
			}

			int getNextChar() {
				return position >= text.length() ? -1 : text.charAt(position++);
			}

		}

		final Parser parser = new Parser(input);

		// This might be replaceable with a quick call to
		// "startsWith(getCommandChar())"...
		final int commandCharLength = getCommandChar().length();
		while (parser.position < commandCharLength)
			if (parser.getNextChar() != getCommandChar().charAt(parser.position - 1))
				return false;

		String command = "";
		boolean quoted = false, backslashed = false;

		int c;
		// Parse the command.
		while (true) {
			c = parser.getNextChar();

			// If we've reached the end of the line (in this command name parser) then just
			// call the command with no args.
			if (c == -1) {
				if (backslashed)
					command += '\\';
				return runCommand(command, new String[0]);
			}

			if (backslashed) {
				if (c == '\\')
					command += '\\';
				else if (c == '"')
					command += '"';
				else
					command += '\\';
				backslashed = false;
			} else if (c == '\\')
				backslashed = true;
			else if (quoted) {
				if (c == '"')
					quoted = false;
				else
					command += (char) c;
			} else if (c == '"')
				quoted = true;
			else if (Character.isWhitespace(c))
				break;
			else
				command += (char) c;

		}

		// The above while loop ends with the character of whitespace right after the
		// command, so we start after that.
		//
		// "/test something"
		// We are at the "s" in "something"

		// Also, these two loops are very similar... but are repeated... // TODO

		final List<String> args = new ArrayList<>();
		String arg = "";

		backslashed = false;
		quoted = false;

		System.out.println(parser.position + " " + (char) c);
		while (Character.isWhitespace(c = parser.getNextChar()))
			;

		System.out.println(parser.position + " " + (char) c);

		while (true) {

			if (c == -1) {
				if (backslashed)
					arg += '\\';
				args.add(arg);
				return runCommand(command, args.toArray(new String[0]));
			}

			if (backslashed) {
				if (c == '\\')
					arg += '\\';
				else if (c == '"')
					arg += '"';
				else
					arg += "\\";
				backslashed = false;
			} else if (c == '\\')
				backslashed = true;
			else if (quoted) {
				if (c == '"')
					quoted = false;
				else
					arg += (char) c;
			} else if (c == '"')
				quoted = true;
			else if (Character.isWhitespace(c)) {
				while (Character.isWhitespace(c = parser.getNextChar()))
					;

				args.add(arg);
				arg = "";

				if (c == -1)
					return runCommand(command, args.toArray(new String[0]));
				continue;// Skip the c=parser.getNextChar() below.
			} else
				arg += (char) c;
			c = parser.getNextChar();
		}

	}

	/**
	 * Runs a command where the command's name is the first item in the string array
	 * and the command's arguments are the remaining items in the string array.
	 *
	 * @param args The string array containing the command name and the given
	 *             arguments.
	 * @return <code>true</code> if the input command was matched to a command in
	 *         this {@link CommandManager}, false otherwise.
	 */
	public final boolean runCommand(final String... args) {
		String name;

		// Get the command's name.
		if (args.length == 0)
			name = "";
		else
			name = args[0];

		// Make an args array.
		final String[] newArgs = new String[args.length - 1];

		// Populate the new args array.
		for (int i = 1; i < args.length; i++)
			newArgs[i - 1] = args[i];

		return runCommand(name, newArgs);
	}

	public boolean runCommand(final String cmd, final String... args) {
		if (hasConsumer()) {
			consumers.pop().consume(cmd, args);
			return true;
		}
		for (final Command c : commands)
			if (c.match(cmd)) {
				c.act(cmd, args);
				return true;
			}
		return false;
	}

	public void setCommandChar(final String commandChar) {
		this.commandChar = commandChar;
	}
}
