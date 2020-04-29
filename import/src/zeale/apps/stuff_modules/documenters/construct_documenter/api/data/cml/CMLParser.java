package zeale.apps.stuff_modules.documenters.construct_documenter.api.data.cml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.alixia.javalibrary.streams.CharacterParser;
import org.alixia.javalibrary.streams.CharacterStream;

public class CMLParser {

	public static class MalformedCMLException extends Exception {
		/**
		 * SUID
		 */
		private static final long serialVersionUID = 1L;
		public final char violator;

		private MalformedCMLException(final char violator) {
			this.violator = violator;
		}

		private MalformedCMLException(final String message, final char violator) {
			super(message);
			this.violator = violator;
		}

		private MalformedCMLException(final Throwable cause, final char violator) {
			super(cause);
			this.violator = violator;
		}

	}

	private int clearWhitespace(final CharacterParser stream) {
		int c;
		while (Character.isWhitespace(c = stream.next()))
			;
		return c;
	}

	/**
	 * <p>
	 * Parses the next {@link Node} from the given {@link InputStream}. If there is
	 * no next node, <code>null</code> is returned.
	 * </p>
	 * <p>
	 * Note that this method uses an {@link InputStreamReader} to read the given
	 * {@link InputStream}, and, thus, may read ahead along the {@link InputStream}.
	 * </p>
	 *
	 * @param input   The {@link InputStream} to parse from.
	 * @param charset The {@link Charset} to use.
	 * @return The {@link Node} that was parsed, or <code>null</code> if none was
	 *         found.
	 * @throws IOException           If an {@link IOException} occurs while parsing.
	 * @throws MalformedCMLException If the input is malformed.
	 */
	public Node parse(final InputStream input, final Charset charset) throws IOException, MalformedCMLException {
		try (InputStreamReader reader = new InputStreamReader(input, charset)) {
			return parseNode(CharacterParser.from(reader));
		}
	}

	private Node parseNode(final CharacterParser stream) throws MalformedCMLException {
		final int c = clearWhitespace(stream);
		if (c == -1)
			return null;
		if (c != '<')
			throw new MalformedCMLException(
					"Expected the opening chevron of a node's header tag, but found: '" + (char) c + "'.", (char) c);
		return parseNodeWithOpenHead(stream);
	}

	public List<Node> parseNodes(final InputStream input, final Charset charset)
			throws IOException, MalformedCMLException {
		try (InputStreamReader reader = new InputStreamReader(input, charset)) {
			final List<Node> nodes = new ArrayList<>();
			Node c;
			final CharacterParser p = CharacterParser.from(reader);
			while ((c = parseNode(p)) != null)
				nodes.add(c);
			return nodes;
		}
	}

	private Node parseNodeWithOpenHead(final CharacterParser stream) throws MalformedCMLException {
		return parseOpenNode(stream, parseOpenHead(stream));
	}

	/**
	 * Parses a header for a tag starting immediately after the opening chevron,
	 * (<code>'<'</code>), character.
	 *
	 * @param stream The {@link CharacterStream} to parse from.
	 * @return The name between the chevrons. (The tag's name.) This returned result
	 *         is <b>trimmed</b> of leadinga and trailing whitespace.
	 * @throws MalformedCMLException In case the input is malformed, (i.e. the end
	 *                               of the stream is encountered before the end of
	 *                               the tag, or some other issue.
	 */
	private String parseOpenHead(final CharacterParser stream) throws MalformedCMLException {
		boolean escaped = false;
		final StringBuilder chars = new StringBuilder();
		int c;
		while ((c = stream.next()) != -1)
			if (c == '\\') {
				if (escaped) {
					chars.append('\\');
					escaped = false;
				} else
					escaped = true;
			} else if (c == '>') {
				if (escaped) {
					chars.append('>');
					escaped = false;
				} else
					return chars.toString().trim();
			} else {
				if (escaped)
					chars.append('\\');
				escaped = false;
				chars.append((char) c);
			}
		throw new MalformedCMLException("Unexpected end of element head.", (char) 0);
	}

	private Node parseOpenNode(final CharacterParser stream, final String nodeName) throws MalformedCMLException {
		final Node n = new Node(nodeName, false);

		int c = clearWhitespace(stream);
		if (c == '<') {
			final List<Node> children = new ArrayList<>(2);
			while (true) {
				// Go into Node parsing sequence. We also need to check for the "</>" tag.
				String r = parseOpenHead(stream);
				if (r.isEmpty())
					throw new MalformedCMLException("Found an empty tag.", '>');
				else if (r.startsWith("/")) {
					r = r.substring(1);
					if (r.isEmpty() || r.equals(nodeName)) {
						if (!children.isEmpty())
							n.setChildren(children);
						return n;
					}
					throw new MalformedCMLException("Found a closing tag for a node that is not currently open.",
							r.charAt(0));
				} else {
					children.add(parseOpenNode(stream, r));
					c = clearWhitespace(stream);
					if (c != '<')
						throw new MalformedCMLException(
								"Found a non-child object inside a node that already has children.", (char) c);
				}
			}
		}

		final StringBuilder b = new StringBuilder();
		b.append((char) c);
		while ((c = stream.next()) != -1) {
			if (c == '<') {
				final String r = parseOpenHead(stream);
				if (!r.startsWith("/"))
					throw new MalformedCMLException(
							"Expected a closing tag to terminate a Node which contained text, but found <" + r
									+ "> after the opening chevron.",
							r.isEmpty() ? 0 : r.charAt(0));
				if (r.length() != 1 && !r.substring(1).equals(nodeName))
					throw new MalformedCMLException(
							"Found a closing tag for a node that was not the currently opened node.", r.charAt(1));
				n.value(b.toString().trim());
				return n;
			}
			b.append((char) c);
		}
		throw new MalformedCMLException("Unexpected premature end of input when parsing a CML object.", (char) 0);

	}
}
