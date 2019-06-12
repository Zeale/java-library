package org.alixia.javalibrary.files;

import java.io.File;

public final class FileTools {
	private FileTools() {	}
	
	/**
	 * Deletes each file and its children if the file is a directory.
	 * 
	 * @param files The files to delete.
	 */
	public static final void deltree(File... files) {
		for(File f:files)
			if(f.isDirectory())
				deltree(f.listFiles());
			else
				f.delete();
	}
	
}
