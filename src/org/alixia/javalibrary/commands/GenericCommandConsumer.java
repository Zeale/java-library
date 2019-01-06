package org.alixia.javalibrary.commands;

public interface GenericCommandConsumer<D> {
	void act(D data);
}
