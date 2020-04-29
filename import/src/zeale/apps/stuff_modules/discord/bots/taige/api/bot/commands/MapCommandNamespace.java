package zeale.apps.stuff_modules.discord.bots.taige.api.bot.commands;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MapCommandNamespace<D, N> extends CommandNamespace<D, N> {
	private final Map<N, MapCommandNamespace<D, N>> children = new HashMap<>();

	public void addSubNamespace(N name, MapCommandNamespace<D, N> space) {
		children.put(name, space);
	}

	public Map<N, MapCommandNamespace<D, N>> getChildren() {
		return children;
	}

	@Override
	public CommandNamespace<D, N> getSubNamespace(N namespaceQuery) {
		return children.get(namespaceQuery);
	}

	@Override
	public Collection<? extends MapCommandNamespace<D, N>> listChildren() {
		return children.values();
	}

}
