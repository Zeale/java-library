package org.alixia.javalibrary.javafx.images;

import java.io.InputStream;
import java.util.function.Consumer;

import org.alixia.javalibrary.javafx.images.Images.LoadedImageReceiver.Receiver.ImageType;
import org.alixia.javalibrary.taskloader.TaskLoader;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Images {

	private final static TaskLoader<ImageLoadingTask> loader = new TaskLoader<>(ImageLoadingTask::handle);

	public interface ImageLoadingTask {
		void handle();
	}

	public static class BasicImageLoadingTask implements ImageLoadingTask {

		public BasicImageLoadingTask(InputStream inputStream, LoadedImageReceiver receiver) {
			this.inputStream = inputStream;
			this.receiver = receiver;
		}

		private final LoadedImageReceiver receiver;
		private final InputStream inputStream;

		@Override
		public void handle() {
			try {
				receiver.imageHandler.receive(new Image(inputStream), ImageType.SUCCESS);
			} catch (Exception e) {
				receiver.imageHandler.receive(getRandomMissingTextureIcon(), ImageType.FAILURE);
			}
		}

	}

	private static Image[] getMissingTextureIcons() {
		return MISSING_TEXTURE_IMAGES;
	}

	private static final String PRIVATE_GRAPHICS_LOCATION = "/branch/alixia/kröw/unnamed/resources/graphics/";

	// This array's initialization may need to be run in a dedicated thread if too
	// many large images are being used as Missing Texture Graphics.
	private static final Image[] MISSING_TEXTURE_IMAGES = new Image[] {
			new Image(PRIVATE_GRAPHICS_LOCATION + "Missing Texture 1.png", true),
			new Image(PRIVATE_GRAPHICS_LOCATION + "Missing Texture 2.png", true) };

	public static Image getRandomMissingTextureIcon() {
		Image[] missingTextureIcons = getMissingTextureIcons();

		return missingTextureIcons.length == 0 ? null
				: missingTextureIcons[(int) (Math.random() * missingTextureIcons.length)];
	}

	public static class AdvancedImageLoadingTask extends BasicImageLoadingTask {

		private final double requestedWidth, requestedHeight;
		private final boolean preserveRatio, smooth;

		public AdvancedImageLoadingTask(InputStream inputStream, LoadedImageReceiver receiver, double requestedWidth,
				double requestedHeight, boolean preserveRatio, boolean smooth) {
			super(inputStream, receiver);
			this.requestedWidth = requestedWidth;
			this.requestedHeight = requestedHeight;
			this.preserveRatio = preserveRatio;
			this.smooth = smooth;
		}

		@Override
		public void handle() {
			try {
				super.receiver.imageHandler.receive(
						new Image(super.inputStream, requestedWidth, requestedHeight, preserveRatio, smooth),
						ImageType.SUCCESS);
			} catch (Exception e) {
				super.receiver.imageHandler.receive(getRandomMissingTextureIcon(), ImageType.FAILURE);
			}
		}

	}

	/**
	 * This class decides what to do with the loaded {@link Image} once it is
	 * finished loading.
	 * 
	 * @author Zeale
	 *
	 */
	public static class LoadedImageReceiver {
		public @FunctionalInterface interface Receiver {
			public enum ImageType {
				SUCCESS, FAILURE, PENDING;
			}

			void receive(Image image, ImageType type);

		}

		private final Receiver imageHandler;

		public LoadedImageReceiver(ImageView view) {
			this(view::setImage);
		}

		public LoadedImageReceiver(Consumer<Image> onReceived) {
			this((image, type) -> onReceived.accept(image));
		}

		public LoadedImageReceiver(Receiver receiver) {
			receiver.receive(getRandomMissingTextureIcon(), ImageType.PENDING);
			this.imageHandler = receiver;
		}

	}

	/**
	 * Loads an {@link Image} in a background thread using the background thread
	 * loading option provided by the {@link Image} API. This method will <b>not</b>
	 * have the same error handling features as the API provided by this class.
	 * 
	 * @param url
	 *            The url pointing to the image.
	 * @return The loaded {@link Image}.
	 * @see Image#Image(String, boolean)
	 */
	public static Image loadImageInBackground(String url) {
		try {
			return new Image(url, true);
		} catch (Exception e) {
			e.printStackTrace();
			return getRandomMissingTextureIcon();
		}
	}

	public static Image loadImageInBackground(String url, double requestedWidth, double requestedHeight,
			boolean preserveRatio, boolean smooth) {
		return new Image(url, requestedWidth, requestedHeight, preserveRatio, smooth, true);
	}

	public static void loadImageInBackground(InputStream inputStream, LoadedImageReceiver receiver) {
		loader.addTask(new BasicImageLoadingTask(inputStream, receiver));
	}

	public static void loadImageInBackground(InputStream inputStream, LoadedImageReceiver receiver,
			double requestedWidth, double requestedHeight, boolean preserveRatio, boolean smooth) {
		loader.addTask(new AdvancedImageLoadingTask(inputStream, receiver, requestedWidth, requestedHeight,
				preserveRatio, smooth));
	}

	/**
	 * Convenience method to load an image in the background and then put it into an
	 * {@link ImageView}.
	 * 
	 * @param inputStream
	 *            An {@link InputStream} through which the {@link Image} will be
	 *            obtained.
	 * @param imageView
	 *            The {@link ImageView} to put the loaded {@link Image} into.
	 */
	public static void loadImageInBackground(InputStream inputStream, ImageView imageView) {
		loadImageInBackground(inputStream, new LoadedImageReceiver(imageView));
	}

	public static void loadImageInBackground(String url, ImageView imageView) {
		imageView.setImage(loadImageInBackground(url));
	}

	public static ImageView loadImageViewInBackground(String url) {
		return new ImageView(loadImageInBackground(url));
	}

	public static ImageView loadImageViewInBackground(String url, double requestedWidth, double requestedHeight,
			boolean preserveRatio, boolean smooth) {
		return new ImageView(loadImageInBackground(url, requestedWidth, requestedHeight, preserveRatio, smooth));
	}

}
