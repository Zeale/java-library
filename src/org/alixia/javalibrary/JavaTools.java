package org.alixia.javalibrary;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Stack;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.alixia.javalibrary.util.Pair;

import zeale.applicationss.notesss.utilities.Utilities;

public final class JavaTools {
	private JavaTools() {
	}

	public static long bytesToLong(byte... bytes) {
		return ((long) bytes[0] & 0xff) << 56 | ((long) bytes[1] & 0xff) << 48 | ((long) bytes[2] & 0xff) << 40
				| ((long) bytes[3] & 0xff) << 32 | ((long) bytes[4] & 0xff) << 24 | ((long) bytes[5] & 0xff) << 16
				| ((long) bytes[6] & 0xff) << 8 | (long) bytes[7] & 0xff;
	}

	public static byte[] longToBytes(long l) {
		return new byte[] { (byte) (l >>> 56), (byte) (l >>> 48), (byte) (l >>> 40), (byte) (l >>> 32),
				(byte) (l >>> 24), (byte) (l >>> 16), (byte) (l >>> 8), (byte) l };
	}

	public static byte[] intToBytes(int i) {
		return new byte[] { (byte) (i >>> 24), (byte) (i >>> 16), (byte) (i >>> 8), (byte) i };
	}

	public static int bytesToInt(byte... bytes) {
		return ((int) bytes[0] & 0xff) << 24 | ((int) bytes[1] & 0xff) << 16 | ((int) bytes[2] & 0xff) << 8
				| (int) bytes[3] & 0xff;
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

	public static <E> Iterable<E> iterable(Iterator<E> itr) {
		return () -> itr;
	}

	public static <E> Iterable<E> iterable(E[] arr) {
		return () -> new Iterator<E>() {

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

	/**
	 * Converts and adds each element in the <code>from</code> matrix to the
	 * <code>test</code> collection.
	 * 
	 * @param <FE>      The type of element in the source object.
	 * @param <TE>      The type of the element in the destination
	 *                  {@link Collection}.
	 * @param <T>       The type of the destination.
	 * @param from      The source {@link Iterable}.
	 * @param converter The converter, used to convert between <code>FE</code>s and
	 *                  <code>TE</code>s.
	 * @param dest      The destination {@link Collection}.
	 * @return The destination {@link Collection}.
	 */
	public static <FE, TE, T extends Collection<? super TE>> T addAll(Iterable<? extends FE> from,
			Function<FE, TE> converter, T dest) {
		for (FE fe : from)
			dest.add(converter.apply(fe));
		return dest;
	}

	@SafeVarargs
	public static <T> T[] addToArray(T[] arr, T... items) {
		requireNonNull(arr, items);
		T[] res = Utilities.array(arr.length + items.length, arr);
		System.arraycopy(items, 0, res, arr.length, items.length);
		return res;
	}

	@SafeVarargs
	public static <T> T[] combine(T[]... arrays) {
		int ts = 0;
		for (int i = 0; i < arrays.length; i++)
			ts += arrays[i].length;
		@SuppressWarnings("unchecked")
		T[] res = (T[]) Array.newInstance(arrays.getClass().getComponentType().getComponentType(), ts);
		for (int i = 0, j = 0; i < arrays.length; j += arrays[i++].length)
			System.arraycopy(arrays[i], 0, res, j, arrays[i].length);
		return res;
	}

	public static byte[] addToArray(byte[] arr, byte... items) {
		requireNonNull(arr, items);
		byte[] newArr = new byte[arr.length + items.length];
		System.arraycopy(arr, 0, newArr, 0, arr.length);
		System.arraycopy(items, 0, newArr, 0, items.length);
		return newArr;
	}

	/**
	 * Throws a {@link NullPointerException} if any of the objects provided are
	 * <code>null</code>.
	 * 
	 * @param objects The array of objects.
	 * @author Gartham
	 */
	public static void requireNonNull(Object... objects) {
		for (Object o : objects)
			if (o == null)
				throw null;
	}

	@SuppressWarnings("unchecked")
	public static <F, T> T[] convert(Function<? super F, ? extends T> converter, F... fs) {
		if (fs == null)
			return null;
		else if (fs.length == 0)
			return (T[]) new Object[0];
		else {
			T first;
			T[] res;
			int i = 0;
			LOOP: {
				for (; i < fs.length; i++) {
					first = converter.apply(fs[i]);
					if (first != null) {
						res = (T[]) Array.newInstance(first.getClass(), fs.length);
						break LOOP;
					}
				}
				return (T[]) new Object[fs.length];
			}
			res[i] = first;
			for (; i < fs.length; i++)
				res[i] = converter.apply(fs[i]);
			return res;
		}
	}

	@SafeVarargs
	public static <T> Iterable<T> iterable(Iterable<? extends T>... iterables) {
		return () -> iterator(iterables);
	}

	public static <T> Iterator<T> iterator(Iterable<? extends T>... iterables) {
		@SuppressWarnings("unchecked")
		Iterator<? extends T>[] itrs = new Iterator[iterables.length];
		for (int i = 0; i < iterables.length; i++)
			itrs[i] = iterables[i].iterator();
		return concat(itrs);
	}

	public static <T> Iterator<T> concat(Iterator<? extends T>... iterators) {
		return new Iterator<T>() {
			int i = 0;

			@Override
			public void remove() {
				if (i < iterators.length)
					iterators[i].remove();
			}

			@Override
			public boolean hasNext() {
				for (; i < iterators.length; i++)
					if (iterators[i].hasNext())
						return true;
				return false;
			}

			@Override
			public T next() {
				if (!hasNext())
					throw new NoSuchElementException();
				else
					return iterators[i].next();
			}
		};
	}

}
