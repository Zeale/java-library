package zeale.mouse.utils;

public abstract class BufferedParser<T> implements Parser<T> {

	private boolean cached;
	private T cache;

	@Override
	public final T peek() {
		if (cached)
			return cache;
		else {
			cached = true;
			return cache = read();
		}
	}

	public final T next() {
		if (cached) {
			cached = false;
			return cache;
		} else
			return read();
	}

	protected abstract T read();

}
