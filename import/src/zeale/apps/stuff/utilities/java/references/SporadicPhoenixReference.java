package zeale.apps.stuff.utilities.java.references;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public abstract class SporadicPhoenixReference<T> {
	private WeakReference<T> reference;

	protected abstract T generate() throws Exception;

	public SporadicPhoenixReference() {
		this(true);
	}

	public static class RegenerationException extends RuntimeException {

		private RegenerationException(Throwable cause) {
			super(cause);
		}

	}

	public static <T> SporadicPhoenixReference<T> create(Supplier<? extends T> generator) {
		return new SporadicPhoenixReference<T>() {
			@Override
			protected T generate() {
				return generator.get();
			}
		};
	}

	public static <T> SporadicPhoenixReference<T> create(boolean lazy, Supplier<? extends T> generator) {
		return new SporadicPhoenixReference<T>(lazy) {

			@Override
			protected T generate() {
				return generator.get();
			}
		};
	}
	
	public static <T> SporadicPhoenixReference<T> create(Callable<? extends T> generator) {
		return new SporadicPhoenixReference<T>() {
			@Override
			protected T generate() throws Exception {
				return generator.call();
			}
		};
	}

	public static <T> SporadicPhoenixReference<T> create(boolean lazy, Callable<? extends T> generator) {
		return new SporadicPhoenixReference<T>(lazy) {

			@Override
			protected T generate() throws Exception {
				return generator.call();
			}
		};
	}

	public boolean exists() {
		return reference.get() != null;
	}

	public SporadicPhoenixReference(boolean lazy) throws RegenerationException {
		try {
			reference = new WeakReference<>(lazy ? null : generate());
		} catch (Exception e) {
			throw new RegenerationException(e);
		}
	}

	public T get() throws RegenerationException {
		T value = reference.get();
		if (value == null) {
			T newValue;
			try {
				newValue = generate();
			} catch (Exception e) {
				throw new RegenerationException(e);
			}
			reference = new WeakReference<>(newValue);
			return newValue;
		} else
			return value;
	}

}
