package zeale.apps.stuff_modules.discord.bots.taige.api.bot.data.formats.ldf;

import java.util.LinkedHashMap;
import java.util.Map;

public class LDFItem {
	private final String name;
	private final Map<String, Object> properties = new LinkedHashMap<>();

	public LDFItem(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

}
