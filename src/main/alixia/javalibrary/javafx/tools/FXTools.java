package main.alixia.javalibrary.javafx.tools;

import org.alixia.chatroom.api.fx.tools.ChatRoomFXTools;

import branch.alixia.kröw.unnamed.tools.UnnamedFXTools;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

public final class FXTools {
	public static final Color DEFAULT_WINDOW_COLOR = new Color(0.34, 0.34, 0.34, 1);
	public static final Color ITEM_BORDER_COLOR = Color.BLUE;
	public static final Color SECONDARY_WINDOW_BORDER_COLOR = ITEM_BORDER_COLOR.interpolate(DEFAULT_WINDOW_COLOR, 0.5);
	public static final double COMMON_BORDER_WIDTH = 2;

	private FXTools() {
	}

	public static Background getBackgroundFromColor(final Color color) {
		return ChatRoomFXTools.getBackgroundFromColor(color);
	}

	/**
	 * Allows the user to drag the given {@link Node} to move the given
	 * {@link javafx.stage.Window}.
	 *
	 * @param window
	 *            The {@link javafx.stage.Window} that will be moved when the
	 *            {@link Node} is dragged.
	 * @param node
	 *            The {@link javafx.stage.Window} that the user will drag to move
	 *            the given {@link Stage}.
	 */
	public static void setPaneDraggableByNode(final javafx.stage.Window window, final Node node) {
		ChatRoomFXTools.setPaneDraggableByNode(window, node);
	}

	/**
	 * <p>
	 * Sets this application's {@link Application#stage} as draggable by the
	 * specified {@link Node}.
	 * <p>
	 * The {@link Node#setOnMousePressed(javafx.event.EventHandler)} and
	 * {@link Node#setOnMouseDragged(javafx.event.EventHandler)} methods are called
	 * on the given {@link Node} to allow the current {@link Application#stage}
	 * object to be moved via the user dragging the given {@link Node}.
	 *
	 * @param node
	 *            The {@link Node} that will be used to move the WindowManager.
	 */
	public static void setPaneDraggableByNode(final Node node, final Stage stage) {
		ChatRoomFXTools.setPaneDraggableByNode(node, stage);
	}

	/**
	 * Spawns a floating piece of text that flies upwards a little then disappears.
	 * The source point of the text is specified via the {@code x} and {@code y}
	 * parameters.
	 *
	 * @param text
	 *            The text to render.
	 * @param color
	 *            The color of the rendered text.
	 * @param x
	 *            The starting x position of the text.
	 * @param y
	 *            The starting y position of the text.
	 */
	public static void spawnLabel(final String text, final Color color, final double x, final double y,
			final Stage stage) {
		ChatRoomFXTools.spawnLabel(text, color, x, y, stage);
	}

	public static void spawnLabelAtMousePos(final String text, final Color color, final Stage stage) {
		ChatRoomFXTools.spawnLabelAtMousePos(text, color, stage);
	}

	public static void addPopup(Node node, Parent popupRoot, Window stage) {
		ChatRoomFXTools.addPopup(node, popupRoot, stage);
	}

	public static Label addHoverText(Node node, String text, Window stage) {
		return ChatRoomFXTools.addHoverText(node, text, stage);
	}

	public static Label addHoverText(Node node, String text, Color backgroundColor, Window stage) {
		return ChatRoomFXTools.addHoverText(node, text, backgroundColor, stage);
	}

	public static Background getBackgroundFromColor(Paint color) {
		return UnnamedFXTools.getBackgroundFromColor(color);
	}

	public static Background getBackgroundFromColor(Paint color, double radius) {
		return UnnamedFXTools.getBackgroundFromColor(color, radius);
	}

	public static Border getBorderFromColor(Paint color) {
		return UnnamedFXTools.getBorderFromColor(color);
	}

	public static Border getBorderFromColor(Paint color, double width) {
		return UnnamedFXTools.getBorderFromColor(color, width);
	}

	public static Border getBorderFromColor(Paint color, double width, double radii) {
		return UnnamedFXTools.getBorderFromColor(color, width, radii);
	}

	public static Transition applyHoverColorAnimation(Shape shape, Duration duration, Color... colors) {
		return UnnamedFXTools.applyHoverColorAnimation(shape, duration, colors);
	}

	public static Transition buildColorwheelTransition(Shape shape, Duration duration, Color... colors) {

		return UnnamedFXTools.buildColorwheelTransition(shape, duration, colors);
	}

	public static Transition buildColorwheelTransition(Shape shape, Color... colors) {
		return UnnamedFXTools.buildColorwheelTransition(shape, colors);
	}

	public static Transition buildColorwheelTransition(Shape shape, Duration duration) {
		return UnnamedFXTools.buildColorwheelTransition(shape, duration);
	}

	public static Transition buildColorwheelTransition(Shape shape) {
		return UnnamedFXTools.buildColorwheelTransition(shape);
	}

	public static Transition applyHoverColorAnimation(Shape shape, Color... colors) {
		return UnnamedFXTools.applyHoverColorAnimation(shape, colors);
	}

	public static Transition applyHoverColorAnimation(Shape shape) {
		return UnnamedFXTools.applyHoverColorAnimation(shape);
	}

	public static void styleBasicInput(Region... inputs) {
		UnnamedFXTools.styleBasicInput(inputs);
	}

	public static void setDefaultBackground(Region item) {
		UnnamedFXTools.setDefaultBackground(item);
	}

}
