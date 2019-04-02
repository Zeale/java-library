package org.alixia.javalibrary.commands.processing;

import java.util.LinkedList;
import java.util.List;

import org.alixia.javalibrary.commands.StringCommand;
import org.alixia.javalibrary.streams.CharacterStream;

/**
 * This class is not threadsafe. :P
 * 
 * @author Zeale
 *
 */
public class StringCommandParser {

	private String commandInitiator;

	public StringCommandParser(String commandChar) {
		if ((commandInitiator = commandChar) == null)
			throw new IllegalArgumentException();
	}

	public StringCommand parse(String input) {
		CharacterStream stream = CharacterStream.from(input);

		for (int i = 0; i < commandInitiator.length(); i++)
			if (stream.next() != commandInitiator.charAt(i))
				return null;

		StringBuilder command = new StringBuilder();

		List<String> args = new LinkedList<>();

		int c;
		while ((c = stream.next()) == ' ')
			;

		boolean quote = false, escaped = false;
		StringBuilder currArg = new StringBuilder();
		while (c != -1) {
			c = stream.next();
			if (c == '\\') {
				if (!(escaped ^= true))
					currArg.append('\\');
			} else if (c == '"') {
				if (escaped) {
					currArg.append('"');
					escaped = false;
				} else
					quote ^= true;
			} else if (Character.isWhitespace(c)) {
				if (quote) {
					if (escaped) {
						currArg.append("\\");
						escaped = false;
					}
					currArg.append(c);
				} else {
					args.add(currArg.toString());
					currArg.setLength(0);
				}
			} else {
				if (escaped) {
					currArg.append("\\");
					escaped = false;
				}
				currArg.append(c);
			}
		}
		args.add(currArg.toString());

		return new StringCommand(command.toString(), input, args.toArray(new String[args.size()]));

	}
}
