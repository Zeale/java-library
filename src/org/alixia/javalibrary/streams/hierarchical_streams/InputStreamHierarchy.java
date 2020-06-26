package org.alixia.javalibrary.streams.hierarchical_streams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface InputStreamHierarchy {
	InputStream getStream(String subpath) throws IOException;

	InputStreamHierarchy getChild(String subpath);

	static InputStreamHierarchy fromFile(File file) {
		return new FileInputStreamHierarchy(file);
	}
}
