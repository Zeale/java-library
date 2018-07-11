package org.alixia.javalibrary.javafx.properties;

import javafx.beans.property.SimpleObjectProperty;

public class NonNullSimpleObjectProperty<T> extends SimpleObjectProperty<T> {

	public NonNullSimpleObjectProperty() {
	}

	public NonNullSimpleObjectProperty(T initialValue) {
		super(initialValue);
	}

	public NonNullSimpleObjectProperty(Object bean, String name) {
		super(bean, name);
	}

	public NonNullSimpleObjectProperty(Object bean, String name, T initialValue) {
		super(bean, name, initialValue);
	}

	@Override
	public void set(T newValue) throws IllegalArgumentException {
		if (newValue == null)
			throw new IllegalArgumentException("Property value cannot be null.");
		super.set(newValue);
	}

}
