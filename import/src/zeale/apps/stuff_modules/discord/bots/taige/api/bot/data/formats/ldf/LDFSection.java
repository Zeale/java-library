package zeale.apps.stuff_modules.discord.bots.taige.api.bot.data.formats.ldf;

import java.util.List;

public class LDFSection {
	private final String name;
	private final List<LDFItem> items;

	public LDFSection(String name, List<LDFItem> items) {
		this.name = name;
		this.items = items;
	}

	public List<LDFItem> getItems() {
		return items;
	}

	public String getName() {
		return name;
	}
}
