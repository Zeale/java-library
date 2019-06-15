package zeale.apps.stuff.utilities.java.references;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public abstract class PhoenixReference<T> extends SporadicPhoenixReference<T> implements Supplier<T> {

	protected abstract T generate();

	public PhoenixReference() {
		this(true);
	}

	public PhoenixReference(boolean lazy) {
		super(lazy);
	}

	public static <T> PhoenixReference<T> create(Supplier<? extends T> generator) {
		return new PhoenixReference<T>() {
			@Override
			protected T generate() {
				return generator.get();
			}
		};
	}

	public static <T> PhoenixReference<T> create(boolean lazy, Supplier<? extends T> generator) {
		return new PhoenixReference<T>(lazy) {

			@Override
			protected T generate() {
				return generator.get();
			}
		};
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

	public void regenerate() {
		reference = new WeakReference<>(generate());
	}

}
