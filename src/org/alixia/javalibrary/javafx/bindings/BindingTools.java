package org.alixia.javalibrary.javafx.bindings;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

import org.alixia.javalibrary.util.Gateway;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;

public final class BindingTools {
	private BindingTools() {
	}

	public static <F, T> void bind(ObservableValue<F> from, Function<? super F, ? extends T> converter,
			Property<T> to) {
		to.bind(Bindings.createObjectBinding(() -> converter.apply(from.getValue()), from));
	}

	/**
	 * Creates a bidirectional binding between the two given observable & writable
	 * values. Whenever one property is changed externally, the new value of that
	 * property is passed to the given {@link Gateway}. The result is then assigned
	 * to the other property. If an invocation of the {@link Gateway} throws a
	 * {@link RuntimeException}, the change is ignored. (This is useful if you want
	 * a property only to update when its pairly bound property is changed to a
	 * value that can be converted to something assignable to the former property).
	 * The resulting {@link PipewayBinding} accesses its observable & writable value
	 * through {@link WeakReference}s.
	 * 
	 * @param <F>     The type of value that the first observable & writable value
	 *                object can hold.
	 * @param <T>     The type of value that the second observable & writable value
	 *                object can hold.
	 * @param <X1>    The type of the first observable & writable value object.
	 * @param <X2>    The type of the second observable & writable value object.
	 * @param from    The first observable & writable value.
	 * @param gateway The converter.
	 * @param to      The second observable & writable value.
	 * @return A {@link PipewayBinding} that can be used to unbind the properties.
	 */
	public static <F, T, X1 extends ObservableValue<? extends F> & WritableValue<? super F>, X2 extends ObservableValue<? extends T> & WritableValue<? super T>> PipewayBinding<F, T, X1, X2> bindBidirectional(
			X1 from, Gateway<F, T> gateway, X2 to) {
		return new PipewayBinding<F, T, X1, X2>(from, to, gateway);
	}

	public static <F, T, X1 extends ObservableValue<? extends F> & WritableValue<? super F>, X2 extends ObservableValue<? extends T> & WritableValue<? super T>> PipewayBinding<F, T, X1, X2> bibindDbg(
			X1 from, Gateway<F, T> gateway, X2 to) {
		return new PipewayBinding<F, T, X1, X2>(from, to, gateway, a -> a.printStackTrace());
	}

	public static <F, T> ObservableValue<T> mask(ObservableValue<F> prop, Function<? super F, ? extends T> converter) {
		return new ObservableValue<T>() {

			private ObservableValue<T> val = this;

			class IncrementetiveChangeListener implements ChangeListener<F> {

				private int count;

				private final ChangeListener<? super T> backing;

				public IncrementetiveChangeListener(ChangeListener<? super T> backing) {
					this.backing = backing;
				}

				@Override
				public void changed(ObservableValue<? extends F> observable, F oldValue, F newValue) {
					for (int i = 0; i < count; i++)
						backing.changed(val, converter.apply(oldValue), converter.apply(newValue));
				}

			}

			Map<ChangeListener<? super T>, IncrementetiveChangeListener> listeners = new WeakHashMap<>();

			@Override
			public void addListener(InvalidationListener listener) {
				prop.addListener(listener);
			}

			@Override
			public void removeListener(InvalidationListener listener) {
				prop.removeListener(listener);
			}

			@Override
			public void addListener(ChangeListener<? super T> listener) {
				if (listeners.containsKey(listener))
					listeners.get(listener).count++;
				else
					listeners.put(listener, new IncrementetiveChangeListener(listener));
			}

			@Override
			public void removeListener(ChangeListener<? super T> listener) {
				if (listeners.containsKey(listener))
					if (listeners.get(listener).count-- < 0)
						listeners.remove(listener);
			}

			@Override
			public T getValue() {
				return converter.apply(prop.getValue());
			}
		};
	}

	public static final class PipewayBinding<F, T, X1 extends ObservableValue<? extends F> & WritableValue<? super F>, X2 extends ObservableValue<? extends T> & WritableValue<? super T>> {
		private final WeakReference<X1> from;
		private final WeakReference<X2> to;
		private final Gateway<F, T> converter;
		private boolean updating;

		private final ChangeListener<F> fromListener = new ChangeListener<F>() {

			@Override
			public void changed(ObservableValue<? extends F> observable, F oldValue, F newValue) {
				if (updating)
					return;
				X1 f = from.get();
				X2 t = to.get();

				if (t == null)
					f.removeListener(this);
				else
					try {
						updating = true;
						T val;
						try {
							val = converter.from(newValue);
						} catch (RuntimeException e) {
							errHandler.accept(e);
							return;
						}
						t.setValue(val);
					} finally {
						updating = false;
					}
			}
		};

		private final ChangeListener<T> toListener = new ChangeListener<T>() {

			@Override
			public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
				if (updating)
					return;
				X1 f = from.get();
				X2 t = to.get();

				if (f == null)
					t.removeListener(this);
				else
					try {
						updating = true;
						F val;
						try {
							val = converter.to(newValue);
						} catch (RuntimeException e) {
							errHandler.accept(e);
							return;
						}
						f.setValue(val);
					} finally {
						updating = false;
					}
			}
		};

		public void unbind() {
			X1 f = from.get();
			if (f != null)
				f.removeListener(fromListener);
			X2 t = to.get();
			if (t != null)
				t.removeListener(toListener);
		}

		public PipewayBinding(X1 prop1, X2 prop2, Gateway<F, T> converter) {
			this(prop1, prop2, converter, a -> {
			});
		}

		private final Consumer<? super RuntimeException> errHandler;

		public PipewayBinding(X1 prop1, X2 prop2, Gateway<F, T> converter,
				Consumer<? super RuntimeException> errHandler) {
			this.from = new WeakReference<>(prop1);
			this.to = new WeakReference<>(prop2);
			this.converter = converter;
			prop1.addListener(fromListener);
			prop2.addListener(toListener);
			this.errHandler = errHandler;
		}

	}

}
