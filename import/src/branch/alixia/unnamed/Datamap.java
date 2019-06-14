package branch.alixia.unnamed;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class Datamap extends HashMap<String, String> {

	// 1. Values can contain "=", but keys cannot.
	// 2. "//" is used for comments; lines that begin with this are skipped over.
	// 3. Neither keys nor values can contain the line separator ("\n"), which is
	// used for separating different pieces of data.

	@Override
	// TODO Trim output.
	public String put(String key, String value) {
		if (key.contains("=") || key.contains("\n") || value.contains("\n"))
			throw new IllegalArgumentException();
		return super.put(key, value);
	}

	/**
	 * SUID
	 */
	private static final long serialVersionUID = 1L;

	public static void save(Datamap datamap, OutputStream output) {
		PrintWriter writer = new PrintWriter(output);
		for (Entry<String, String> e : datamap.entrySet())
			writer.println(e.getKey() + "=" + e.getValue());
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
		try (Scanner scanner = new Scanner(inputStream)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.isEmpty() || line.startsWith("//"))
					continue;
				else if (!line.contains("="))
					continue;
				int index = line.indexOf('=');
				String key = line.substring(0, index), value = line.substring(index + 1);

				put(key, value);
			}
		}
	}

	public void update(InputStream inputStream) {
		try (Scanner scanner = new Scanner(inputStream)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.isEmpty() || line.startsWith("//"))
					continue;
				else if (!line.contains("="))
					throw new RuntimeException("Couldn't find a key value pair for a line.");
				int index = line.indexOf('=');
				String key = line.substring(0, index), value = line.substring(index + 1);

				put(key, value);
			}
		}
	}

}
