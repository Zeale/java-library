package org.alixia.javalibrary.io.files.monitor;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.WatchService;

public class FileMonitor {
	private final WatchService service;

	/**
	 * <p>
	 * Constructs a new {@link FileMonitor} based on the given {@link FileSystem}.
	 * (This FileMonitor API uses an underlying {@link WatchService} to efficiently
	 * get file changes, and the given {@link FileSystem} is used to instantiate the
	 * {@link WatchService}.)
	 * </p>
	 * 
	 * @param filesystem The {@link FileSystem} that this {@link FileMonitor} will
	 *                   monitor files in.
	 * @throws UnsupportedOperationException In case the given {@link FileSystem}
	 *                                       does not support {@link WatchService}s.
	 * @throws IOException                   If creating a {@link WatchService},
	 *                                       (which this API uses for file
	 *                                       watching), fails due to an
	 *                                       {@link IOException}, (as thrown by
	 *                                       {@link FileSystem#newWatchService()}).
	 * @throws NullPointerException          In case the given {@link FileSystem} is
	 *                                       <code>null</code>, (or if creating a
	 *                                       {@link WatchService} throws a
	 *                                       {@link NullPointerException}).
	 */
	public FileMonitor(FileSystem filesystem) throws UnsupportedOperationException, IOException, NullPointerException {
		service = filesystem.newWatchService();
	}

	/**
	 * <p>Constructs a new {@link FileMonitor} based on the default {@link FileSystem}.
	 * 
	 * @throws UnsupportedOperationException
	 * @throws NullPointerException
	 * @throws IOException
	 */
	public FileMonitor() throws UnsupportedOperationException, NullPointerException, IOException {
		this(FileSystems.getDefault());
	}

}
