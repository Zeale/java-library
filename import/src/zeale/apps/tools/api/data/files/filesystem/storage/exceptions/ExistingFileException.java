package zeale.apps.tools.api.data.files.filesystem.storage.exceptions;

public class ExistingFileException extends FileStorageException {

	/**
	 * SUID
	 */
	private static final long serialVersionUID = 1L;

	public ExistingFileException() {
	}

	public ExistingFileException(String message) {
		super(message);
	}

	public ExistingFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExistingFileException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ExistingFileException(Throwable cause) {
		super(cause);
	}

}
