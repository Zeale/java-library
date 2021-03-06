package zeale.apps.tools.api.data.files.filesystem.storage;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;

import zeale.apps.tools.api.data.files.filesystem.storage.exceptions.ExistingFileException;
import zeale.apps.tools.api.data.files.filesystem.storage.exceptions.FileStorageException;

public class FileStorage {

	/**
	 * This method attempts to create a {@link FileStorage} with the first given
	 * {@link String} as a basing path. If this process fails, (see
	 * {@link #create(String)} for explanation on "fail"), this method will attempt
	 * to create another {@link FileStorage}, in the same manner as the first, using
	 * the second given {@link String} as a basing path, if any.
	 * 
	 * @param paths The paths to try to create this {@link FileStorage} with, in
	 *              order.
	 * @return A working {@link FileStorage} based off the first path {@link String}
	 *         given, which worked, or <code>null</code> otherwise.
	 */
	public static FileStorage create(String... paths) {
		for (String s : paths) {
			FileStorage storage = create(s);
			if (storage != null)
				return storage;
		}
		return null;
	}

	/**
	 * <p>
	 * Attempts to create a {@link FileStorage} based on the specified path. If the
	 * creation of the actual {@link FileStorage} on the filesystem fails, this
	 * method will return <code>null</code>.
	 * </p>
	 * <p>
	 * More opaquely, this method attempts to instantiate a {@link FileStorage}. If
	 * the newly instantiated {@link FileStorage}'s {@link #getException()} method
	 * returns a value that is not <code>null</code>, this method returns
	 * <code>null</code>.
	 * </p>
	 * 
	 * @param path The path that the created {@link FileStorage} will represent (if
	 *             creation succeeds).
	 * @return <code>null</code> if the {@link FileStorage} creation failed.
	 *         Otherwise, this method returns the created {@link FileStorage}.
	 */
	public static FileStorage create(String path) {
		FileStorage storage = new FileStorage(new File(path));
		return storage.getException() == null ? null : storage;
	}

	public class Data {
		private final File file;
		private Exception exception;

		protected Data(File file) {
			this.file = file;
		}

		/**
		 * <p>
		 * Creates the {@link File} on the filesystem that this {@link Data} object
		 * represents.
		 * </p>
		 * <p>
		 * This method is meant to be used like such:
		 *
		 * <pre>
		 * <code>
		 *
		 * 	Data data = ...;
		 * 	if(data.create()) // This is done each time the file is about to be used. Calling create() repeatedly doesn't do anything bad.
		 * 		System.out.println("The file exists.");
		 * 	else
		 * 		System.out.println("The file does not exist.");
		 *
		 * </code>
		 * </pre>
		 *
		 * <code>data.create()</code> should be "checked" each time the application
		 * wants to use the underlying file.
		 * </p>
		 *
		 * @return <code>true</code> if this method is successful, <code>false</code>
		 *         otherwise.
		 */
		public boolean create() {
			if (file != null && isAvailable())
				return true;
			file.getParentFile().mkdirs();
			try {
				Files.createFile(file.toPath());
				return true;
			} catch (Exception e) {
				exception = e;
			}
			return false;
		}

		public Exception getException() {
			return exception;
		}

		public File getFile() {
			return file;
		}

		public boolean isAvailable() {
			return file.isFile();
		}
	}

	public class SubStorage extends FileStorage {
		protected SubStorage(String path) {
			super(new File(FileStorage.this.file.toString() + "/" + path), false);
			// Cancer
//			System.out.println(file);
//			System.out.println(FileStorage.this.file);
//			System.out.println(super.file);
			if (!super.file.getParentFile().equals(FileStorage.this.file))
				throw new RuntimeException(
						"Cannot make a doubly nested SubStorage. A SubStorage should be a direct child to its parent.");
			create();
		}

		/**
		 * Returns the parent of this {@link SubStorage}. This storage's parent might
		 * also be a {@link SubStorage}, but this method returns a {@link FileStorage}.
		 * If you know for a fact that the parent to this {@link SubStorage} is a
		 * {@link SubStorage}, (perhaps, due to an <code>instanceof</code> check), feel
		 * free to cast the returned value of this method.
		 *
		 * @return This {@link FileStorage}'s parent.
		 */
		public FileStorage getParent() {
			return FileStorage.this;
		}
	}

	private final File file;

	private FileStorageException exception;

	public FileStorage(File file) {
		this(file, true);
	}

	public FileStorage(String path) {
		this(new File(path));
	}

	private FileStorage(File file, boolean create) {
		if (file == null)
			throw null;
		if (create)
			create();
		this.file = file;
	}

	/**
	 * @return <code>true</code> if
	 */
	public boolean create() {
		if (file != null && isAvailable())
			return true;
		try {
			file.mkdirs();
			Files.createDirectory(file.toPath());
			return true;
		} catch (FileAlreadyExistsException e) {
			exception = new ExistingFileException(e);
		} catch (Exception e) {
			exception = new FileStorageException(e);
		}
		return false;
	}

	/**
	 * This method always returns a {@link SubStorage} object. The usability of that
	 * object can be determined by calling {@link SubStorage#isAvailable()} on it.
	 *
	 * @param path The sub path of the child.
	 * @return A new {@link SubStorage} object representing the sub-directory.
	 */
	public SubStorage createChild(String path) {
		return new SubStorage(path);
	}

	/**
	 * <p>
	 * Creates a file denoted by the name and returns it. Returns <code>null</code>
	 * upon failure.
	 * </p>
	 * <p>
	 * Even if this method fails to create the file, some of the file's parent
	 * directories may have been created.
	 * </p>
	 *
	 * @param name The name of the file. Must have an extension, however the portion
	 *             before the extension can be blank.
	 * @return A {@link Data} object representing the created file, or
	 *         <code>null</code>, if the file could not be created (AKA if the
	 *         {@link Data#create()} method returns false).
	 */
	public Data createFile(String name) {
		if (name == null)
			throw null;
		if (!name.contains("."))
			throw new RuntimeException("Invalid file name. Files must contain an extension.");

		Data data = new Data(new File(file.toPath() + "/" + name));
		return data.create() ? data : null;
	}

	/**
	 * <p>
	 * Gets the last exception thrown when {@link #create() creating} this file
	 * storage's folder on the filesystem. {@link #create()} is called once when a
	 * {@link FileStorage} object is first created.
	 * </p>
	 * <p>
	 * Please note that this method will not remove the cached exception, much like
	 * in some other APIs. This means that calling {@link #getException()} twice on
	 * the same object, without {@link #create()} being called on that object in
	 * between, would render the same exception, twice.
	 * </p>
	 *
	 * @return The last {@link FileStorageException} exception thrown.
	 */
	public FileStorageException getException() {
		return exception;
	}

	public File getFile() {
		return file;
	}

	public boolean isAvailable() {
		return file.isDirectory();
	}

	/**
	 * Clears the current exception (if any) and returns it.
	 * 
	 * @return The exception that was cleared from this {@link FileStorage}
	 */
	private FileStorageException clearException() {
		FileStorageException exception = getException();
		this.exception = null;
		return exception;
	}

	private void setException(FileStorageException exception) {
		this.exception = exception;
	}

}
