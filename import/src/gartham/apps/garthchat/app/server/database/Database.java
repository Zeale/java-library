package gartham.apps.garthchat.app.server.database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Database {
	Database subDatabase(String path);

	OutputStream writer(String filename) throws IOException;

	InputStream reader(String filename) throws IOException;

	String[] list();

	static Database filesystem(File file) {
		return new FilesystemDatabase(file);
	}

	boolean isFile(String filename);

}
