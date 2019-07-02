package org.alixia.javalibrary.javafx.bindings;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

import org.alixia.chatroom.api.QuickList;
import org.alixia.javalibrary.util.Gateway;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

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
							val = converter.to(newValue);
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
							val = converter.from(newValue);
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

	/**
	 * Creates a filtered binding between the two given {@link List}s. Each change
	 * in the <code>container</code> list is applied through each given
	 * <code>filter</code>. If any filter returns <code>false</code> when given the
	 * item involved in a change, the change is ignored (i.e. the
	 * <code>boundContainer</code> is not affected by the change observed in the
	 * container). If the change is an addition, each filter must return
	 * <code>true</code> when given the object being added for the addition to be
	 * reflected in the <code>boundContainer</code>. If the change is a removal,
	 * each filter must still return <code>true</code> for the removal to be
	 * reflected in the <code>boundContainer</code>. Changes are observed happening
	 * in the <code>container</code> and are reflected (if the filters allow) in the
	 * <code>boundContainer</code>.
	 * 
	 * @param <T>            The type of the content of the bound list.
	 * @param container      The {@link List} to observe.
	 * @param boundContainer The {@link List} to apply observed changes that
	 *                       successfully pass through the filters, to.
	 * @param filters        The filters that allow a change to be propagated to the
	 *                       bound list.
	 * @return A {@link FilterBinding} object which can be used to modify the
	 *         filters involved in the binding or to unbind/rebind the lists at
	 *         will.
	 */
	@SafeVarargs
	public static <T> FilterBinding<T> filterBind(ObservableList<? extends T> container, List<T> boundContainer,
			Function<T, Boolean>... filters) {
		return new FilterBinding<>(container, boundContainer, filters);
	}

	public static final class FilterBinding<T> {

		private final ObservableList<? extends T> container;
		private final List<T> glass;
		private final Collection<Function<? super T, Boolean>> filters;
		private final ListChangeListener<T> listener;

		public synchronized void addFilter(Function<? super T, Boolean> filter) {
			filters.add(filter);
			for (Iterator<T> iterator = glass.iterator(); iterator.hasNext();)
				if (!filter.apply(iterator.next()))
					iterator.remove();
		}

		public synchronized void removeFilter(Function<? super T, Boolean> filter) {
			filters.remove(filter);

			NEXT_ITEM: for (T t : container)
				if (!filter.apply(t)) {
					for (Function<? super T, Boolean> f : filters)
						if (!f.apply(t))
							continue NEXT_ITEM;
					glass.add(t);
				}
		}

		public synchronized void refresh() {
			glass.clear();
			NEXT_ITEM: for (T t : container) {
				for (Function<? super T, Boolean> f : filters)
					if (!f.apply(t))
						continue NEXT_ITEM;
				glass.add(t);
			}
		}

		@SafeVarargs
		public FilterBinding(ObservableList<? extends T> container, List<T> glass,
				Function<? super T, Boolean>... filters) {
			this.glass = glass;
			this.filters = new QuickList<Function<? super T, Boolean>>(filters);
			synchronized (this) {
				glass.clear();
				N: for (T t : container) {
					for (Function<? super T, Boolean> f : filters)
						if (!f.apply(t))
							continue N;
					glass.add(t);
				}
				(this.container = container).addListener(listener = (ListChangeListener<T>) c -> {
					while (c.next())
						if (c.wasAdded())
							NEXT_ITEM: for (T t1 : c.getAddedSubList())
								synchronized (this) {
									for (Function<? super T, Boolean> f1 : this.filters)
										if (!f1.apply(t1))
											continue NEXT_ITEM;
									glass.add(t1);
								}
						else if (c.wasRemoved())
							NEXT_ITEM: for (T t2 : c.getRemoved())
								synchronized (this) {
//									if (FilterBinding.this.filters.size() < glass.size()) {
									for (Function<? super T, Boolean> f2 : this.filters)
										if (!f2.apply(t2))
											continue NEXT_ITEM;
									glass.remove(t2);
//									} else
//										glass.remove(t);
								}
				});
			}

		}

		public synchronized void unbind() {
			container.removeListener(listener);
		}

		public synchronized void bind() {
			glass.clear();
			N: for (T t : container) {
				for (Function<? super T, Boolean> f : filters)
					if (!f.apply(t))
						continue N;
				glass.add(t);
			}
			container.addListener(listener);
		}

	}

	public static <T> ObservableListView<T> view(ObservableList<? extends T> list) {
		return new ObservableListView<>(list);
	}

	public static <T> ObservableListView<T> view(ObservableListView<T> view) {
		return new ObservableListView<>(view);
	}

	public static <F, T> ObservableListView<T> view(ObservableListView<? extends F> view, Function<F, T> converter) {
		return new ObservableListView<T>() {
			{
				view.addListener(new ListListener<F>() {

					@Override
					public void added(List<? extends F> items, int startpos) {
						List<T> list = new ArrayList<>(items.size());
						for (F f : items)
							list.add(converter.apply(f));
						propAdd(list, startpos);

					}

					@Override
					public void removed(List<? extends F> items, int startpos) {
						List<T> list = new ArrayList<>(items.size());
						for (F f : items)
							list.add(converter.apply(f));
						propRem(list, startpos);
					}
				});
			}
		};
	}

	public interface Unbindable {
		void unbind();
	}

	public static <T> Unbindable bind(Collection<? super T> list, ObservableListView<? extends T> view) {
		ListListener<T> listener = new ListListener<T>() {

			@Override
			public void added(List<? extends T> items, int startpos) {
				list.addAll(items);
			}

			@Override
			public void removed(List<? extends T> items, int startpos) {
				for (T t : items)
					list.remove(t);
			}
		};
		view.addListener(listener);
		return () -> view.removeListener(listener);
	}

	/**
	 * Returns an {@link ObservableListView} which propagates the changes in the
	 * specified {@link ObservableListView} that pass the given filters. <b>Changes
	 * propagated past this filter will be be broken, in terms of position. I.e.,
	 * the positions of changes propagated from this view should not be expected to
	 * be accurate.</b>
	 * 
	 * @param <T>     The type of the object of the list that gets changed.
	 * @param view    The view to filter changes from.
	 * @param filters The filters.
	 * @return A new {@link ObservableListView}.
	 */
	@SafeVarargs
	public static <T> ObservableListView<T> filter(ObservableListView<? extends T> view,
			Function<? super T, Boolean>... filters) {
		return new ObservableListView<T>() {
			{
				view.addListener(new ListListener<T>() {

					@Override
					public void added(List<? extends T> items, int startpos) {
						List<T> prop = new ArrayList<>(items.size());
						NEXT_ITEM: for (T t : items) {
							for (Function<? super T, Boolean> f : filters)
								if (!f.apply(t))
									continue NEXT_ITEM;
							prop.add(t);
						}

						propAdd(prop, -1);
					}

					@Override
					public void removed(List<? extends T> items, int startpos) {
						List<T> prop = new ArrayList<>(items.size());
						NEXT_ITEM: for (T t : items) {
							for (Function<? super T, Boolean> f : filters)
								if (!f.apply(t))
									continue NEXT_ITEM;
							prop.add(t);
						}

						propRem(prop, -1);
					}
				});
			}
		};
	}

}
