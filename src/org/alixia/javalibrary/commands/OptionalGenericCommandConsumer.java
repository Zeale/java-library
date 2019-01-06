package org.alixia.javalibrary.commands;

public interface OptionalGenericCommandConsumer<D> {
	boolean act(D data);
}
