package zeale.applicationss.notesss.utilities.generators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public interface Generator<R> {
	@SafeVarargs
	static <R, T extends R> Generator<R> arrayLoop(T... array) {
		if (array.length == 0)
			throw new IllegalArgumentException("Cannot create a Generator using an array of size 0.");
		return new Generator<R>() {

			private int pos = -1;

			@Override
			public T next() {
				return array[++pos >= array.length ? (pos = 0) : pos];
			}
		};
	}

	static <R, T extends R> Generator<R> loop(Iterable<T> itr) {
		return new Generator<R>() {

			private Iterator<T> i = itr.iterator();

			@Override
			public R next() {
				if (!i.hasNext())
					i = itr.iterator();
				return i.next();
			}
		};
	}

	@SafeVarargs
	static <R, T extends R> Generator<R> random(T... array) {
		return () -> array[(int) (Math.random() * array.length)];
	}

	R next();

	default void collect(int count, Collection<? super R> collection) {
		for (; count > 0; count--)
			collection.add(next());
	}

	default ArrayList<R> collect(int count) {
		ArrayList<R> list = new ArrayList<>(count);
		collect(count, list);
		return list;
	}

}
