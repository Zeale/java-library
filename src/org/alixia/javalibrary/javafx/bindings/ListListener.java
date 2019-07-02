package org.alixia.javalibrary.javafx.bindings;

import java.util.List;

import javafx.collections.ListChangeListener;

public interface ListListener<T> extends ListChangeListener<T> {
	void added(List<? extends T> items, int startpos);

	void removed(List<? extends T> items, int startpos);

	@Override
	default void onChanged(Change<? extends T> c) {
		while (c.next())
			if (c.wasAdded())
				added(c.getAddedSubList(), c.getFrom());
			else if (c.wasRemoved())
				removed(c.getRemoved(), c.getFrom());
	}
}
