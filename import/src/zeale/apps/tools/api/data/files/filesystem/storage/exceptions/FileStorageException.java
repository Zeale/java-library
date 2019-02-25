package zeale.apps.tools.api.data.files.filesystem.storage.exceptions;

public class FileStorageException extends Exception {

	/**
	 * SUID
	 */
	private static final long serialVersionUID = 1L;

	public FileStorageException() {
	}

	public FileStorageException(String message) {
		super(message);
	}

	public FileStorageException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileStorageException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FileStorageException(Throwable cause) {
		super(cause);
	}

}
