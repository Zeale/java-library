package org.alixia.javalibrary;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.Stack;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.alixia.javalibrary.util.Pair;

public final class JavaTools {
	private JavaTools() {
	}

	@SafeVarargs
	public static final <T> T pickRandomElement(T... ts) {
		return ts[(int) (Math.random() * ts.length)];
	}

	/**
	 * Deletes each file and its children if the file is a directory.
	 * 
	 * @param files The files to delete.
	 */
	public static final void deltree(File... files) {
		for (File f : files)
			if (f.isDirectory())
				deltree(f.listFiles());
			else
				f.delete();
	}

	public static <F, T> Iterator<T> mask(Iterator<? extends F> itr, Function<? super F, ? extends T> conv) {
		return new Iterator<T>() {

			@Override
			public boolean hasNext() {
				return itr.hasNext();
			}

			@Override
			public T next() {
				return conv.apply(itr.next());
			}
		};
	}

	public static <E> Iterable<E> iterable(E[] arr) {
		return new Iterable<E>() {

			@Override
			public Iterator<E> iterator() {
				return new Iterator<E>() {

					int pos;

					@Override
					public boolean hasNext() {
						return pos < arr.length;
					}

					@Override
					public E next() {
						return arr[pos++];
					}
				};
			}
		};
	}

	public static <F, T> Iterable<T> mask(Iterable<? extends F> itr, Function<? super F, ? extends T> conv) {
		return () -> mask(itr.iterator(), conv);
	}

	public static <E> ListIterator<E> unmodifyingListIterator(ListIterator<E> base) {
		return new ListIterator<E>() {

			@Override
			public boolean hasNext() {
				return base.hasNext();
			}

			@Override
			public E next() {
				return base.next();
			}

			@Override
			public boolean hasPrevious() {
				return base.hasNext();
			}

			@Override
			public E previous() {
				return base.previous();
			}

			@Override
			public int nextIndex() {
				return base.nextIndex();
			}

			@Override
			public int previousIndex() {
				return base.previousIndex();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException(
						"This ListIterator can't modify its underlying Collection; that operation is invalid.");
			}

			@Override
			public void set(E e) {
				throw new UnsupportedOperationException(
						"This ListIterator can't modify its underlying Collection; that operation is invalid.");
			}

			@Override
			public void add(E e) {
				throw new UnsupportedOperationException(
						"This ListIterator can't modify its underlying Collection; that operation is invalid.");
			}
		};
	}

	public static <E> Iterator<E> unmodifyingIterator(Iterator<E> itr) {
		return new Iterator<E>() {

			@Override
			public boolean hasNext() {
				return itr.hasNext();
			}

			@Override
			public E next() {
				return itr.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException(
						"This Iterator can't modify its underlying Collection; that operation is invalid.");
			}
		};
	}

	public static <E> Stack<E> unmodifiableStack(Stack<E> stack) {

		return new Stack<E>() {

			/**
			 * SUID
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public synchronized void trimToSize() {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			public synchronized void ensureCapacity(int minCapacity) {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			public synchronized void setSize(int newSize) {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			public synchronized void setElementAt(E obj, int index) {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			public synchronized void removeElementAt(int index) {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			public synchronized void insertElementAt(E obj, int index) {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			public synchronized void addElement(E obj) {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			public synchronized boolean removeElement(Object obj) {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			public synchronized void removeAllElements() {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			public synchronized E set(int index, E element) {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			public synchronized boolean add(E e) {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			public boolean remove(Object o) {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			public void add(int index, E element) {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			public synchronized E remove(int index) {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			public void clear() {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			public synchronized boolean addAll(Collection<? extends E> c) {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			public synchronized boolean removeAll(Collection<?> c) {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			public synchronized boolean retainAll(Collection<?> c) {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			public synchronized boolean addAll(int index, Collection<? extends E> c) {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			public synchronized List<E> subList(int fromIndex, int toIndex) {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			protected synchronized void removeRange(int fromIndex, int toIndex) {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			public synchronized ListIterator<E> listIterator(int index) {
				return unmodifyingListIterator(super.listIterator(index));
			}

			@Override
			public synchronized ListIterator<E> listIterator() {
				return unmodifyingListIterator(super.listIterator());
			}

			@Override
			public synchronized Iterator<E> iterator() {
				return unmodifyingIterator(super.iterator());
			}

			@Override
			public synchronized boolean removeIf(Predicate<? super E> filter) {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			public synchronized void replaceAll(UnaryOperator<E> operator) {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			public synchronized void sort(Comparator<? super E> c) {
				throw new UnsupportedOperationException(
						"This Stack is unmodifiable; the requested operation is invalid.");
			}

			@Override
			public Spliterator<E> spliterator() {
				return super.spliterator();
			}

		};
	}

	public static double findMedian(double... values) {
		double[] copy = values.clone();
		return findMedianUnsafe(copy);
	}

	public static double findMedianUnsafe(double... values) {
		// TODO Improve
		if (values == null)
			throw null;
		if (values.length == 0)
			return 0;
		Arrays.sort(values);
		return (values.length & 1) == 0 ? (values[values.length / 2] + values[values.length / 2 - 1]) / 2
				: values[values.length / 2];
	}

	public static <E extends Comparable<? extends E>> Pair<E, E> findMedianUnsafe(List<? extends E> items) {
		return findMedianUnsafe(items, null);
	}

	public static <E extends Comparable<? extends E>> Pair<E, E> findMedian(List<? extends E> items) {
		return findMedianUnsafe(new ArrayList<>(items));
	}

	public static <E> Pair<E, E> findMedianUnsafe(List<? extends E> items, Comparator<E> comparator) {
		items.sort(comparator);
		return getMidElement(items);
	}

	public static <E> Pair<E, E> findMedian(List<? extends E> items, Comparator<E> comparator) {
		return findMedianUnsafe(new ArrayList<>(items), comparator);
	}

	/**
	 * Returns a {@link Pair} object if the given list is not empty or
	 * <code>null</code>. Under these circumstances, if the given list's size is
	 * even, the {@link Pair} returned will contain the items in the list at indexes
	 * <code>list.size() / 2 - 1</code> and <code>list.size() / 2</code>,
	 * respectively. If the list's size is odd, the {@link Pair} will consist of the
	 * item in the list at index <code>list.size() / 2</code>, and
	 * <code>null</code>, respectively. If the list is empty, this method returns
	 * <code>null</code>, and if the given list is <code>null</code>, this method
	 * throws <code>null</code>.
	 * 
	 * @param <E>   The type of object held by the given list.
	 * @param items The list of items to get the middle element(s) of.
	 * @return A {@link Pair} consisting of both of the middle elements if the
	 *         list's size is even, or the middle element if the list's size is odd.
	 */
	private static <E> Pair<E, E> getMidElement(List<? extends E> items) {
		if (items == null)
			throw null;
		if (items.isEmpty())
			return null;
		if ((items.size() & 1) == 0) {
			Iterator<? extends E> itr = items.listIterator(items.size() / 2 - 1);
			return new Pair<>(itr.next(), itr.next());
		}
		return new Pair<>(items.get(items.size() / 2), null);
	}

	public static <E> Map<E, Integer> frequencyMap(Iterator<? extends E> itr) {
		Map<E, Integer> freqmap = new HashMap<>();
		for (; itr.hasNext();) {
			E val = itr.next();
			if (freqmap.containsKey(val))
				freqmap.put(val, freqmap.get(val) + 1);
			else
				freqmap.put(val, 1);
		}
		return freqmap;
	}

	public static <E> Map<E, Integer> frequencyMap(Iterable<? extends E> itr) {
		return frequencyMap(itr.iterator());
	}

}
