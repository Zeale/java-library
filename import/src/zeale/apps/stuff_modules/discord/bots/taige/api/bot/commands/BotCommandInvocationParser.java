package zeale.apps.stuff_modules.discord.bots.taige.api.bot.commands;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.alixia.javalibrary.commands.StringCommand;
import org.alixia.javalibrary.streams.CharacterStream;
import org.alixia.javalibrary.strings.matching.Matching;

public class BotCommandInvocationParser {

	private static class CountingCharStream implements CharacterStream {

		private final String string;

		private int pos = 0;

		public CountingCharStream(String string) {
			this.string = string;
		}

		@Override
		public int next() {
			return pos >= string.length() ? -1 : string.codePointAt(pos++);
		}

	}

	private Matching commandInitiator;

	public BotCommandInvocationParser() {
	}

	public BotCommandInvocationParser(Matching commandInitiator) {
		this.commandInitiator = commandInitiator;
	}

	public Matching getCommandInitiator() {
		return commandInitiator;
	}

	/**
	 * Parses a command out of the given {@link String}. This method begins by
	 * attempting to find the {@link #commandInitiator} at the start of the given
	 * {@link String}. If it cannot find an exact match of the
	 * {@link #commandInitiator}, for any reason (including the presence of
	 * whitespace), this method will return <code>null</code>. Otherwise, the method
	 * continues by trimming any whitespace. Once this whitespace, (which is after
	 * the {@link #commandInitiator}), has been passed over, this method splits the
	 * string by unquoted whitespace. The first result of the split is considered
	 * the command, which may, optionally, contain namespaces, separated by unquoted
	 * and unescaped colons (<code>:</code>). If no command is found, the returned
	 * {@link BotCommandInvocation#command TaigeCommandInvocation's command} will be
	 * an empty string. If whitespace is found after the command or after an
	 * argument, but no succeeding argument is found, there will be an empty
	 * {@link String} at the end of the parsed {@link BotCommandInvocation#args}
	 * that is returned by this method.
	 *
	 * @param input The input {@link String} to parse.
	 * @return <code>null</code> or the parsed command as a {@link StringCommand}.
	 */
	public <O> BotCommandInvocation<O> parse(String input, O source) {
		int prefsize, cmdSize = 0;
		String in;
		if (commandInitiator != null) {
			in = commandInitiator.match(input);
			if ((prefsize = input.length() - in.length()) == 0)
				return null;
		} else {
			prefsize = 0;
			in = input;
		}
		final CountingCharStream stream = new CountingCharStream(in);

		final List<String> args = new LinkedList<>(), namespaces = new LinkedList<>();

		int c;

		boolean quote = false, escaped = false;
		final StringBuilder currArg = new StringBuilder();

		// Parse possibly absolute command.

		while ((c = stream.next()) != -1)
			if (c == '\\') {
				if (!(escaped ^= true))
					currArg.append('\\');
			} else if (c == '"')
				if (escaped) {
					currArg.append('"');
					escaped = false;
				} else
					quote ^= true;
			else if (Character.isWhitespace(c))
				if (quote) {
					if (escaped) {
						currArg.append('\\');
						escaped = false;
					}
					currArg.append((char) c);
				} else {
					cmdSize = stream.pos + prefsize;
					args.add(currArg.toString());
					currArg.setLength(0);
					while (Character.isWhitespace(c = stream.next()))
						;
					break;
				}
			else if (c == ':') {
				if (quote) {
					if (escaped) {
						currArg.append('\\');
						escaped = false;
					}
					currArg.append((char) c);
				} else if (escaped) {
					currArg.append(':');
					escaped = false;
				} else {
					namespaces.add(currArg.toString());
					currArg.setLength(0);
				}
			} else {
				if (escaped) {
					currArg.append("\\");
					escaped = false;
				}
				currArg.append((char) c);
			}

		while (c != -1) {
			if (c == '\\') {
				if (!(escaped ^= true))
					currArg.append('\\');
			} else if (c == '"')
				if (escaped) {
					currArg.append('"');
					escaped = false;
				} else
					quote ^= true;
			else if (Character.isWhitespace(c))
				if (quote) {
					if (escaped) {
						currArg.append("\\");
						escaped = false;
					}
					currArg.append((char) c);
				} else {
					if (args.isEmpty())
						cmdSize = stream.pos + prefsize;
					args.add(currArg.toString());
					currArg.setLength(0);
					while (Character.isWhitespace(c = stream.next()))
						;
					continue;
				}
			else {
				if (escaped) {
					currArg.append("\\");
					escaped = false;
				}
				currArg.append((char) c);
			}
			c = stream.next();
		}
		if (args.isEmpty())
			cmdSize = stream.pos + prefsize;
		args.add(currArg.toString());

		final String command = args.get(0), argArr[] = new String[args.size() - 1];
		final Iterator<String> itr = args.iterator();
		if (itr.hasNext()) {
			itr.next();
			for (int i = 0; itr.hasNext(); argArr[i++] = itr.next())
				;
		}

		return new BotCommandInvocation<>(command, input, source, namespaces.toArray(new String[namespaces.size()]),
				prefsize, cmdSize - 1, argArr);

	}

	public void setCommandInitiator(Matching commandInitiator) {
		this.commandInitiator = commandInitiator;
	}

}
