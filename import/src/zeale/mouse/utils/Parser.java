package zeale.mouse.utils;

import java.util.ArrayList;
import java.util.List;

public interface Parser<T> {
	T peek();

	T next();

	default List<T> parseAll() {
		List<T> el = new ArrayList<>();
		while (peek() != null)
			el.add(next());
		return el;
	}
}
