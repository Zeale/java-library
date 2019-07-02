package org.alixia.javalibrary.javafx.bindings;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;

public class ObservableListView<T> implements ListListener<T> {

	public ObservableListView(ObservableList<? extends T> list) {
		list.addListener(this);
	}

	public ObservableListView(ObservableListView<? extends T> view) {
		view.addListener(this);
	}

	protected final void propAdd(List<? extends T> items, int startpos) {
		for (ListListener<? super T> ll : listeners)
			ll.added(items, startpos);
	}

	protected final void propRem(List<? extends T> items, int startpos) {
		for (ListListener<? super T> ll : listeners)
			ll.removed(items, startpos);
	}

	ObservableListView() {
	}

	protected final List<ListListener<? super T>> getListeners() {
		return listeners;
	}

	private final List<ListListener<? super T>> listeners = new ArrayList<>();

	public void addListener(ListListener<? super T> listener) {
		listeners.add(listener);
	}

	public void removeListener(ListListener<? super T> listener) {
		listeners.remove(listener);
	}

	@Override
	public void added(List<? extends T> items, int startpos) {
		propAdd(items, startpos);
	}

	@Override
	public void removed(List<? extends T> items, int startpos) {
		propRem(items, startpos);
	}

}
