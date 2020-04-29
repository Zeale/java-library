package zeale.apps.stuff_modules.discord.bots.taige.api.bot.commands;

import java.util.List;
import java.util.Stack;

import org.alixia.javalibrary.commands.GenericCommand;
import org.alixia.javalibrary.commands.GenericCommandConsumer;
import org.alixia.javalibrary.commands.GenericCommandManager;
import org.alixia.javalibrary.commands.OptionalGenericCommandConsumer;

public abstract class CommandNamespace<D, N> extends GenericCommandManager<D> {
	public final List<GenericCommand<? super D>> commandListView() {
		return commandView;
	}

	public final Stack<GenericCommandConsumer<? super D>> consumerListView() {
		return consumerView;
	}

	public abstract CommandNamespace<D, N> getSubNamespace(N namespaceQuery);

	public abstract Iterable<? extends CommandNamespace<D, N>> listChildren();

	public final Stack<OptionalGenericCommandConsumer<? super D>> optionalConsumerListView() {
		return optionalConsumerView;
	}
}
