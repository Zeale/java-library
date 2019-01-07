package org.alixia.javalibrary.commands;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.alixia.javalibrary.JavaTools;

public class GenericCommandManager<D> {
	private final List<GenericCommand<? super D>> commands = new LinkedList<>();
	private final Stack<GenericCommandConsumer<? super D>> consumers = new Stack<>();
	private final Stack<OptionalGenericCommandConsumer<? super D>> optionalConsumers = new Stack<>();

	protected final List<GenericCommand<? super D>> commandView = Collections.unmodifiableList(commands);
	protected final Stack<GenericCommandConsumer<? super D>> consumerView = JavaTools.unmodifiableStack(consumers);
	protected final Stack<OptionalGenericCommandConsumer<? super D>> optionalConsumerView = JavaTools
			.unmodifiableStack(optionalConsumers);

	public void addCommand(GenericCommand<? super D> command) {
		if (!commands.contains(command))
			commands.add(command);
	}

	public void removeCommand(GenericCommand<? super D> command) {
		commands.remove(command);
	}

	public void addConsumer(GenericCommandConsumer<? super D> consumer) {
		consumers.push(consumer);
	}

	public void removeConsumer(GenericCommandConsumer<? super D> consumer) {
		consumers.remove(consumer);
	}

	public void addConsumer(OptionalGenericCommandConsumer<? super D> consumer) {
		addOptionalConsumer(consumer);
	}

	public void removeConsumer(OptionalGenericCommandConsumer<? super D> consumer) {
		removeOptionalConsumer(consumer);
	}

	public void addOptionalConsumer(OptionalGenericCommandConsumer<? super D> consumer) {
		optionalConsumers.push(consumer);
	}

	public void removeOptionalConsumer(OptionalGenericCommandConsumer<? super D> consumer) {
		optionalConsumers.remove(consumer);
	}

	public boolean run(D data) {

		// Optional consumers are checked,
		if (!optionalConsumers.isEmpty())
			for (Iterator<OptionalGenericCommandConsumer<? super D>> iterator = optionalConsumers.iterator(); iterator
					.hasNext();)
				if (iterator.next().act(data)) {
					iterator.remove();
					return true;
				}

		// then regular consumers are checked,
		if (!consumers.isEmpty()) {
			consumers.pop().act(data);
			return true;
		}

		// then commands are checked.
		for (GenericCommand<? super D> gc : commands)
			if (gc.match(data)) {
				gc.act(data);
				return true;
			}
		return false;
	}
}
