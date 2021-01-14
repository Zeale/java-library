package zeale.applicationss.notesss.utilities.generators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * An implementation of a {@link Generator} that returns <code>null</code> to
 * signify that it has finished.
 *
 * @author Zeale
 *
 * @param <R> The return type of this {@link Generator}.
 */
public abstract class NullstopGenerator<R> implements Generator<R>, Iterator<R> {

	private R element;
	private boolean cached;

	private void cacheNext() {
		cached = true;
		element = next();
	}

	@Override
	public final boolean hasNext() {
		if (!cached)
			cacheNext();
		return element != null;
	}

	@Override
	public final R next() {
		if (cached) {
			cached = false;
			return element;
		}
		return nextItem();
	}

	protected abstract R nextItem();

	@Override
	public void collect(int count, Collection<? super R> collection) {
		for (; count > 0; count--) {
			R next = next();
			if (next == null)
				return;
			collection.add(next);
		}
	}

}
