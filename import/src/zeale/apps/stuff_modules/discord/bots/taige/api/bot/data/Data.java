package zeale.apps.stuff_modules.discord.bots.taige.api.bot.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.alixia.javalibrary.streams.CharacterStream;
import org.alixia.javalibrary.util.Gateway;

import branch.alixia.unnamed.Datamap;

public class Data {

	public static final Gateway<String[], String> ARRAY_TO_STRING_GATEWAY = new Gateway<String[], String>() {

		@Override
		public String[] from(String value) {
			if (value == null)
				return null;
			final List<String> items = new ArrayList<>();
			final CharacterStream s = CharacterStream.from(value);
			int c;
			final StringBuilder sb = new StringBuilder();
			boolean escaped = false;
			while ((c = s.next()) != -1)
				if (c == ',')
					if (escaped)
						sb.append(',');
					else {
						items.add(sb.toString());
						sb.setLength(0);
					}
				else if (c == '\\') {
					if (!(escaped ^= true))
						sb.append('\\');
				} else
					sb.append((char) c);
			if (sb.length() != 0)
				items.add(sb.toString());

			return items.toArray(new String[items.size()]);
		}

		@Override
		public String to(String[] value) {
			if (value == null)
				return null;
			final StringBuilder b = new StringBuilder();
			if (value.length > 0) {
				b.append(value[0].replace("\\", "\\\\").replace(",", "\\,"));
				for (int i = 1; i < value.length; i++) {
					final String s = value[i];
					b.append(",").append(s.replace("\\", "\\\\").replace(",", "\\,"));
				}
			}

			return b.toString();
		}
	};

	private final String id;

	private final Datamap data = new Datamap();

	private final File datafile;

	public Data(File datafile, String id) {
		this.datafile = datafile;
		this.id = id;
	}

	public Datamap getData() {
		return data;
	}

	File getDatafile() {
		return datafile;
	}

	public String getId() {
		return id;
	}

	public void load() throws FileNotFoundException {
		data.update(new FileInputStream(datafile));
	}

	public final void save() throws FileNotFoundException {
		datafile.getParentFile().mkdirs();
		save(new FileOutputStream(datafile));
	}

	public void save(OutputStream file) {
		data.save(file);
	}

	public void update(InputStream file) {
		data.update(file);
	}

}
