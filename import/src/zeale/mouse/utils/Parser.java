package zeale.mouse.utils;

public interface Parser<T> {
	T peek();

	T next();
}
