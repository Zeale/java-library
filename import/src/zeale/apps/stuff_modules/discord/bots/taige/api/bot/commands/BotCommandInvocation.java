package zeale.apps.stuff_modules.discord.bots.taige.api.bot.commands;

import org.alixia.javalibrary.commands.StringCommand;

public class BotCommandInvocation<S> extends StringCommand {

	public final S source;

	public final String namespaces[];
	private final int prefixSize, commandSize;

	public BotCommandInvocation(String command, String inputText, S source, String[] namespaces, int prefixSize,
			int commandSize, String... args) {
		super(command, inputText, args);
		this.source = source;
		this.namespaces = namespaces;
		this.prefixSize = prefixSize;
		this.commandSize = commandSize;
	}

	public String cmd() {
		return getCommand();
	}

	/**
	 * Returns the command portion of the input text.
	 *
	 * @return The part after the prefix, but before the arguments. This includes
	 *         namespace and command and the first whitespace character following
	 *         the command. The rest of the whitespace following the command is
	 *         contained in the {@link #tail()}.
	 */
	public String command() {
		return inputText.substring(prefixSize, prefixSize + commandSize);
	}

	public String[] getArgs() {
		return args;
	}

	public String getCmd() {
		return getCommand();
	}

	public String getCommand() {
		return command;
	}

	public int getCommandSize() {
		return commandSize;
	}

	public S getData() {
		return source;
	}

	public String getInputText() {
		return inputText;
	}

	public String[] getNamespaces() {
		return namespaces;
	}

	public int getPrefixSize() {
		return prefixSize;
	}

	public S getSource() {
		return source;
	}

	public String input() {
		return inputText;
	}

	/**
	 * Returns the prefix portion of the input text.
	 *
	 * @return The prefix used to invoke the command.
	 */
	public String prefix() {
		return inputText.substring(0, prefixSize);
	}

	/**
	 * Returns the argument portion of the input text.
	 *
	 * @return The text following the command name in the input text. This includes
	 *         all but the first whitespace character right before the arguments, if
	 *         any.
	 */
	public String tail() {
		return inputText.substring(prefixSize + commandSize);
	}
}
