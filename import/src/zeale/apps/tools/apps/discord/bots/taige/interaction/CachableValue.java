package zeale.apps.tools.apps.discord.bots.taige.interaction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class CachableValue {
	private String value;
	private final File location;

	public CachableValue(File location) {
		this.location = location;
		readValue();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if (value == null)
			throw null;
		this.value = value;
		writeValue();
	}

	public boolean writeValue() {
		try (PrintWriter out = new PrintWriter(location)) {
			out.println(value);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean readValue() {
		try (Scanner in = new Scanner(location)) {
			if (in.hasNextLine())
				value = in.nextLine();
			else
				return false;
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean cached() {
		try (Scanner in = new Scanner(location)) {
			return in.hasNextLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String readCache(File cache) {
		if (!cache.exists())
			return null;
		try (Scanner in = new Scanner(cache)) {
			return in.hasNextLine() ? in.nextLine() : null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean writeCache(File cache, String line) {
		try (PrintWriter out = new PrintWriter(cache)) {
			out.println(line);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
}
