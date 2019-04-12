package org.alixia.javalibrary.commands;

public class StringCommand {
	public final String command, inputText;
	public final String[] args;

	public StringCommand(String command, String inputText, String... args) {
		this.command = command;
		this.inputText = inputText;
		this.args = args;
	}

}
