package org.alixia.javalibrary.javafx.bindings;

import java.util.function.Function;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;

public final class BindingTools {
	private BindingTools() {
	}

	public static <F, T> void bind(ObservableValue<F> from, Function<? super F, ? extends T> converter,
			Property<T> to) {
		to.bind(Bindings.createObjectBinding(() -> converter.apply(from.getValue()), from));
	}

}
