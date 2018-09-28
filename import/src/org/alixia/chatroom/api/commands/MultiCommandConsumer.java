package org.alixia.chatroom.api.commands;

import java.util.LinkedList;
import java.util.List;

public class MultiCommandConsumer extends CommandConsumer {

	private final List<Command> commands = new LinkedList<>();

	public void addCommands(final Command... commands) {
		for (final Command c : commands)
			if (!this.commands.contains(c))
				this.commands.add(c);
	}

	@Override
	public boolean consume(final String command, final String... args) {
		for (final Command c : commands)
			if (c.match(command)) {
				c.act(command, args);
				return true;
			}

		if (inputUnrecognizedCommand != null)
			inputUnrecognizedCommand.act(command, args);
		return false;
	}

	private Command inputUnrecognizedCommand;

	public Command getInputUnrecognizedCommand() {
		return inputUnrecognizedCommand;
	}

	public void setInputUnrecognizedCommand(Command inputUnrecognizedCommand) {
		this.inputUnrecognizedCommand = inputUnrecognizedCommand;
	}

}
