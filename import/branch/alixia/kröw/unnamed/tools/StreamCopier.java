package branch.alixia.kröw.unnamed.tools;

import java.io.InputStream;
import java.io.OutputStream;

import javafx.beans.value.WritableValue;

public class StreamCopier {

	private final WritableValue<Number> progress;
	private final InputStream input;
	private final OutputStream output;
	private final double inputSize;

	private Throwable exception;

	private boolean started;

	private final Thread thread = new Thread(new Runnable() {
		@Override
		public void run() {

			try {

				final int bufferLength = 1024;
				long totalReadData = 0;

				final byte[] buffer = new byte[bufferLength];
				int amount;
				while ((amount = input.read(buffer)) != -1) {
					totalReadData += amount;
					output.write(buffer, 0, amount);
					progress.setValue(totalReadData / inputSize);
				}
			} catch (final Throwable e) {
				exception = e;
			}
		}
	});

	public StreamCopier(final double inputSize, final WritableValue<Number> progress, final InputStream input,
			final OutputStream output) {
		this.progress = progress;
		this.input = input;
		this.output = output;
		this.inputSize = inputSize;

		start();

	}

	public Throwable getException() {
		return exception;
	}

	public void start() {
		if (!started) {
			started = true;
			thread.start();
		}
	}

}
