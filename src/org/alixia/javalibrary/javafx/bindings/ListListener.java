package org.alixia.javalibrary.javafx.bindings;

import java.util.List;

public interface ListListener<T> {
	void added(List<? extends T> items, int startpos);

	void removed(List<? extends T> items, int startpos);
}
