package zeale.apps.stuff_modules.discord.bots.taige.api.bot.commands;

import java.util.ArrayList;
import java.util.List;

import org.alixia.javalibrary.JavaTools;
import org.alixia.javalibrary.strings.matching.Matching;
import org.alixia.javalibrary.util.Pair;

public class MatchCommandNamespace<D> extends CommandNamespace<D, String> {

	private final List<Pair<Matching, CommandNamespace<D, String>>> children = new ArrayList<>();
	private final Iterable<? extends CommandNamespace<D, String>> childrenListView = JavaTools.mask(children,
			a -> a.second);

	public void addChild(Matching matching, CommandNamespace<D, String> ns) {
		children.add(new Pair<>(matching, ns));
	}

	@Override
	public CommandNamespace<D, String> getSubNamespace(String namespaceQuery) {
		for (final Pair<Matching, CommandNamespace<D, String>> p : children)
			if (p.first.fullyMatches(namespaceQuery))
				return p.second;
		return null;
	}

	@Override
	public Iterable<? extends CommandNamespace<D, String>> listChildren() {
		return childrenListView;
	}

}
