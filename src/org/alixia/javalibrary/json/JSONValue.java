package org.alixia.javalibrary.json;

import java.io.Serializable;

public interface JSONValue extends Serializable {
	String toString(String indentation);
}
