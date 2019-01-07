package org.alixia.javalibrary.commands;

import org.alixia.javalibrary.commands.GenericCommand;

public interface QuickGenericCommand<D> extends GenericCommand<D> {
	@Override
	default boolean match(D data) {
		return run(data);
	}

	boolean run(D data);

	@Override
	default void act(D data) {
	}
}
