package org.alixia.javalibrary.commands.processing;

import java.util.Iterator;
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

	public void setCommandInitiator(String commandInitiator) {
		if (commandInitiator == null)
			throw new IllegalArgumentException();
		this.commandInitiator = commandInitiator;
	}

	public StringCommand parse(String input) {
		CharacterStream stream = CharacterStream.from(input);

		for (int i = 0; i < commandInitiator.length(); i++)
			if (stream.next() != commandInitiator.charAt(i))
				return null;

		List<String> args = new LinkedList<>();

		int c;
		while (Character.isWhitespace(c = stream.next()))
			;

		boolean quote = false, escaped = false;
		StringBuilder currArg = new StringBuilder();
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
		args.add(currArg.toString());

		String command = args.get(0), argArr[] = new String[args.size() - 1];
		Iterator<String> itr = args.iterator();
		if (itr.hasNext()) {
			itr.next();
			for (int i = 0; itr.hasNext(); argArr[i++] = itr.next())
				;
		}

		return new StringCommand(command, input, argArr);

	}
}
