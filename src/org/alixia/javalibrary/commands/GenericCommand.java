package org.alixia.javalibrary.commands;

public interface GenericCommand<D> extends GenericCommandConsumer<D> {
	boolean match(D data);

	void act(D data);
}
