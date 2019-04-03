package org.alixia.javalibrary.commands;

public interface AbstractCommandManager<D> {
	void addCommand(GenericCommand<? super D> cmd);

	void addConsumer(GenericCommandConsumer<? super D> cnsm);

	void addOptionalConsumer(OptionalGenericCommandConsumer<? super D> optnCnsm);

	void removeCommand(GenericCommand<? super D> cmd);

	void removeConsumer(GenericCommandConsumer<? super D> cnsm);

	void removeOptionalConsumer(OptionalGenericCommandConsumer<? super D> optnCnsm);

	default void addConsumer(OptionalGenericCommandConsumer<? super D> optnCnsm) {
		addOptionalConsumer(optnCnsm);
	}

	default void removeConsumer(OptionalGenericCommandConsumer<? super D> optnCnsm) {
		removeOptionalConsumer(optnCnsm);
	}

	boolean run(D data);
}
