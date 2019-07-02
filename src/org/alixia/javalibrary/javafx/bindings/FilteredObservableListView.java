package org.alixia.javalibrary.javafx.bindings;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.alixia.chatroom.api.QuickList;

import javafx.collections.ObservableList;

public class FilteredObservableListView<T> extends ObservableListView<T> {
	private final List<Function<? super T, Boolean>> filters;

	FilteredObservableListView() {
		filters = new ArrayList<>();
	}

	public void addFilter(Function<? super T, Boolean> filter) {
		filters.add(filter);
	}

	public synchronized void removeFilter(Function<? super T, Boolean> filter) {
		filters.remove(filter);
	}

	@Override
	public synchronized void added(List<? extends T> items, int startpos) {
		super.added(filter(items), -1);
	}

	private synchronized List<T> filter(List<? extends T> items) {
		List<T> prop = new ArrayList<>(items.size());
		NEXT_ITEM: for (T t : items) {
			for (Function<? super T, Boolean> f : filters)
				if (!f.apply(t))
					continue NEXT_ITEM;
			prop.add(t);
		}
		return prop;
	}

	@Override
	public void removed(List<? extends T> items, int startpos) {
		super.removed(filter(items), -1);
	}

	@SafeVarargs
	public FilteredObservableListView(ObservableList<? extends T> list, Function<? super T, Boolean>... filters) {
		super(list);
		this.filters = new QuickList<>(filters);
	}

	@SafeVarargs
	public FilteredObservableListView(ObservableListView<? extends T> view, Function<? super T, Boolean>... filters) {
		super(view);
		this.filters = new QuickList<>(filters);
	}

}
