package branch.alixia.unnamed;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.alixia.javalibrary.util.Gateway;
import org.alixia.javalibrary.util.Pair;
import org.alixia.javalibrary.util.StringGateway;

public class Datamap extends HashMap<String, String> {

	protected String escapeKey(String key) {
		return key.replace((CharSequence) "\\", "\\\\").replace("\n", "\\\n").replace("=", "\\=");
	}

	protected String escapeValue(String value) {
		return value.replace("\\", "\\\\").replace("\n", "\\\n");
	}

	protected String splitter = "=", nextPair = "\n";

	/**
	 * SUID
	 */
	private static final long serialVersionUID = 1L;

	public static void save(Datamap datamap, OutputStream output) {
		datamap.save(output);
	}

	public void save(OutputStream output) {
		try (PrintWriter writer = new PrintWriter(output)) {
			write(writer);
		}
	}

	protected void write(PrintWriter writer) {
		for (Map.Entry<String, String> e : entrySet())
			write(writer, e.getKey(), e.getValue());
	}

	protected void write(PrintWriter writer, String key, String value) {
		writer.print(escapeKey(key) + splitter + escapeValue(value) + nextPair);
	}

	public <T> String put(String key, Gateway<? super T, ? extends String> converter, T value) {
		return put(key, converter.from(), value);
	}

	public <T> T get(String key, Gateway<? extends T, ? super String> converter) {
		return get(key, converter.to());
	}

	public <T> String put(String key, Function<? super T, ? extends String> converter, T value) {
		return put(key, converter.apply(value));
	}

	public <T> T get(String key, Function<? super String, ? extends T> converter) {
		return converter.apply(get(key));
	}

	// TODO Trim read input.
	public static Datamap read(InputStream inputStream) {
		Datamap map = new Datamap();
		map.update(inputStream);
		return map;
	}

	public static Datamap readLax(InputStream inputStream) {
		Datamap map = new Datamap();
		map.updateLax(inputStream);
		return map;
	}

	public void updateLax(InputStream inputStream) {
		try (Reader reader = new InputStreamReader(inputStream)) {
			NEXT_KEY: while (true) {
				String key = "";
				boolean escaped = false;
				while (true) {
					int x = reader.read();
					if (x == -1)
						return;// There's a dangling key at the end of the file. Ignore it.
					// Or we just, actually reached the end of the file. Either way...

					char c = (char) x;
					if (c == '\\')
						if (escaped)
							key += '\\';
						else
							escaped = true;
					else if (c == '=')
						if (escaped)
							key += '=';
						else
							break;// Move on to parsing the value
					else if (c == '\n')
						if (escaped)
							key += '\n';
						else
							// Ignore this key. It has no value. (Later, this sort of "error" will represent
							// the key exisiting, but having no value (or having the value null, or
							// something).)
							continue NEXT_KEY;
					else
						key += c;
				}

				String value = "";
				while (true) {
					int x = reader.read();
					if (x == -1) {
						put(key, value);
						return;
					} else if (x == '\n') {
						if (escaped)
							value += '\n';
						else {
							put(key, value);
							continue NEXT_KEY;
						}
					} else
						value += (char) x;
				}

			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void update(InputStream inputStream) {
		try (Reader reader = new InputStreamReader(inputStream)) {
			NEXT_KEY: while (true) {
				String key = "";
				boolean escaped = false;
				while (true) {
					int x = reader.read();
					if (x == -1)
						if (!key.isEmpty())
							throw new RuntimeException("Found a key with no value.");
						else
							return;

					char c = (char) x;
					if (c == '\\')
						if (escaped)
							key += '\\';
						else
							escaped = true;
					else if (c == '=')
						if (escaped)
							key += '=';
						else
							break;// Move on to parsing the value
					else if (c == '\n')
						if (escaped)
							key += '\n';
						else
							// Throw an exception, since this key has no value.
							throw new RuntimeException("Found a key with no value.");
					else
						key += c;
				}

				String value = "";
				while (true) {
					int x = reader.read();
					if (x == -1) {
						put(key, value);
						return;
					} else if (x == '\n')
						if (escaped)
							value += '\n';
						else {
							put(key, value);
							continue NEXT_KEY;
						}
					else
						value += (char) x;
				}

			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static class UpdateException extends RuntimeException {
		private UpdateException(Throwable cause) {
			super(cause);
		}
	}

}
