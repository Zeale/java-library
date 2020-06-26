package org.alixia.javalibrary.streams.hierarchical_streams;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public interface OutputStreamHierarchy {
	OutputStream getStream(String subpath) throws IOException;

	OutputStreamHierarchy getChild(String subpath);

	static OutputStreamHierarchy fromFile(File file) {
		return new FileOutputStreamHierarchy(file);
	}
}
