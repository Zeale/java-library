package org.alixia.javalibrary.commands;

import java.util.LinkedList;
import java.util.List;

public class GenericCommandManager<D> {
	private final List<GenericCommand<? super D>> commands = new LinkedList<>();

	public void run(D data) {
		for (GenericCommand<? super D> gc : commands)
			if (gc.match(data))
				gc.act(data);
	}
}
