package gartham.apps.garthchat.app.server.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FilesystemDatabase implements Database {

	private final File dir;

	public FilesystemDatabase(File dir) {
		this.dir = dir;
	}

	@Override
	public Database subDatabase(String path) {
		return new FilesystemDatabase(new File(dir, path));
	}

	@Override
	public OutputStream writer(String filename) throws IOException {
		dir.mkdirs();
		return new FileOutputStream(new File(dir, filename));
	}

	@Override
	public InputStream reader(String filename) throws IOException {
		return new FileInputStream(new File(dir, filename));
	}

	@Override
	public String[] list() {
		return dir.isDirectory() ? dir.list() : new String[0];
	}

	@Override
	public boolean isFile(String filename) {
		return dir.isDirectory() && new File(dir, filename).isFile();
	}

}
