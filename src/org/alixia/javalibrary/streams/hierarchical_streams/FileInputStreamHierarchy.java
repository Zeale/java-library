package org.alixia.javalibrary.streams.hierarchical_streams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.NotDirectoryException;

class FileInputStreamHierarchy implements InputStreamHierarchy {

	private final File file;

	public FileInputStreamHierarchy(File file) {
		this.file = file;
	}

	@Override
	public InputStream getStream(String subpath) throws NotDirectoryException, FileNotFoundException {
		if (!file.isDirectory())
			throw new NotDirectoryException(
					"The file backing this FileInputStreamHierarchy is not a directory right now.");
		return new FileInputStream(new File(file, subpath));
	}

	@Override
	public InputStreamHierarchy getChild(String subpath) {
		return new FileInputStreamHierarchy(new File(file, subpath));
	}

}
