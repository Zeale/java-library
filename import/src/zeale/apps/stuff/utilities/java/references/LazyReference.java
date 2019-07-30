package zeale.apps.stuff.utilities.java.references;

import java.util.function.Supplier;

public abstract class LazyReference<T> implements Supplier<T> {
	protected abstract T generate();

	private boolean generated;

	private T ref;

	public T get() {
		if (!generated) {
			ref = generate();
			generated = true;
		}
		return ref;
	}

	public static final <T> LazyReference<T> create(Supplier<? extends T> supplier) {
		return new LazyReference<T>() {
			@Override
			protected T generate() {
				return supplier.get();
			}
		};
	}

}
