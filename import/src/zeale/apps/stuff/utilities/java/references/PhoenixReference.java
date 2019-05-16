package zeale.apps.stuff.utilities.java.references;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

public abstract class PhoenixReference<T> {

	private WeakReference<T> reference;

	protected abstract T generate();

	{
		reference = new WeakReference<>(generate());
	}

	public static <T> PhoenixReference<? super T> create(java.util.function.Supplier<? extends T> generator) {
		return new PhoenixReference<T>() {
			@Override
			protected T generate() {
				return generator.get();
			}
		};
	}

	public boolean exists() {
		return reference.get() != null;
	}

	public final T get() {
		// #reference should always have a value.
		T value = reference.get();
		if (value == null) {
			T newValue = generate();
			reference = new WeakReference<>(newValue);
			return newValue;
		} else
			return value;
	}

}
