package org.alixia.javalibrary.streams.hierarchical_streams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.NotDirectoryException;

class FileOutputStreamHierarchy implements OutputStreamHierarchy {

	private final File file;

	public FileOutputStreamHierarchy(File file) {
		this.file = file;
	}

	@Override
	public OutputStream getStream(String subpath) throws IOException {
		if (!file.isDirectory())
			throw new NotDirectoryException(
					"The file backing this FileInputStreamHierarchy is not a directory right now.");
		file.mkdirs();
		return new FileOutputStream(new File(file, subpath));
	}

	@Override
	public OutputStreamHierarchy getChild(String subpath) {
		return new FileOutputStreamHierarchy(new File(file, subpath));
	}

}
