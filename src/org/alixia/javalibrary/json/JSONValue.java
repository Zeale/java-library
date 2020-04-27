package org.alixia.javalibrary.json;

import java.io.Serializable;
import java.util.Collection;

public interface JSONValue extends Serializable {
	String toString(String indentation);

	static <T extends Collection<? super String>> void addAll(Collection<? extends JSONValue> c, T t) {
		for (JSONValue j : c)
			t.add(j.toString());
	}
}
