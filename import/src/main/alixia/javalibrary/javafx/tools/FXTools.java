package main.alixia.javalibrary.javafx.tools;

import org.alixia.chatroom.api.fx.tools.ChatRoomFXTools;

import branch.alixia.kröw.unnamed.tools.UnnamedFXTools;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.beans.binding.NumberExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

public final class FXTools {
	public static final Color DEFAULT_WINDOW_COLOR = new Color(0.34, 0.34, 0.34, 1);
	public static final Color ITEM_BORDER_COLOR = Color.BLUE;
	public static final Color SECONDARY_WINDOW_BORDER_COLOR = ITEM_BORDER_COLOR.interpolate(DEFAULT_WINDOW_COLOR, 0.5);
	public static final double COMMON_BORDER_WIDTH = 2;

	public static void setAllAnchors(double anchorDistance, Node... nodes) {
		setAllAnchors(anchorDistance, anchorDistance, anchorDistance, anchorDistance, nodes);
	}

	public static void setAllAnchors(double top, double right, double bottom, double left, Node... nodes) {
		for (Node n : nodes) {
			AnchorPane.setTopAnchor(n, top);
			AnchorPane.setRightAnchor(n, right);
			AnchorPane.setBottomAnchor(n, bottom);
			AnchorPane.setLeftAnchor(n, left);
		}

	}

	public static Label addHoverText(final Node node, final String text, final Color backgroundColor,
			final Window stage) {
		return ChatRoomFXTools.addHoverText(node, text, backgroundColor, stage);
	}

	public static Label addHoverText(final Node node, final String text, final Window stage) {
		return ChatRoomFXTools.addHoverText(node, text, stage);
	}

	public static void addPopup(final Node node, final Parent popupRoot, final Window stage) {
		ChatRoomFXTools.addPopup(node, popupRoot, stage);
	}

	public static Transition applyHoverColorAnimation(final Shape shape) {
		return UnnamedFXTools.applyHoverColorAnimation(shape);
	}

	public static Transition applyHoverColorAnimation(final Shape shape, final Color... colors) {
		return UnnamedFXTools.applyHoverColorAnimation(shape, colors);
	}

	public static Transition applyHoverColorAnimation(final Shape shape, final Duration duration,
			final Color... colors) {
		return UnnamedFXTools.applyHoverColorAnimation(shape, duration, colors);
	}

	public static Transition buildColorwheelTransition(final Shape shape) {
		return UnnamedFXTools.buildColorwheelTransition(shape);
	}

	public static Transition buildColorwheelTransition(final Shape shape, final Color... colors) {
		return UnnamedFXTools.buildColorwheelTransition(shape, colors);
	}

	public static Transition buildColorwheelTransition(final Shape shape, final Duration duration) {
		return UnnamedFXTools.buildColorwheelTransition(shape, duration);
	}

	public static Transition buildColorwheelTransition(final Shape shape, final Duration duration,
			final Color... colors) {

		return UnnamedFXTools.buildColorwheelTransition(shape, duration, colors);
	}

	public static Background getBackgroundFromColor(final Color color) {
		return ChatRoomFXTools.getBackgroundFromColor(color);
	}

	public static Background getBackgroundFromColor(final Paint color) {
		return UnnamedFXTools.getBackgroundFromColor(color);
	}

	public static Background getBackgroundFromColor(final Paint color, final double radius) {
		return UnnamedFXTools.getBackgroundFromColor(color, radius);
	}

	public static Border getBorderFromColor(final Paint color) {
		return UnnamedFXTools.getBorderFromColor(color);
	}

	public static Border getBorderFromColor(final Paint color, final double width) {
		return UnnamedFXTools.getBorderFromColor(color, width);
	}

	public static Border getBorderFromColor(final Paint color, final double width, final double radii) {
		return UnnamedFXTools.getBorderFromColor(color, width, radii);
	}

	public static void setDefaultBackground(final Region item) {
		UnnamedFXTools.setDefaultBackground(item);
	}

	/**
	 * Allows the user to drag the given {@link Node} to move the given
	 * {@link javafx.stage.Window}.
	 *
	 * @param window The {@link javafx.stage.Window} that will be moved when the
	 *               {@link Node} is dragged.
	 * @param node   The {@link javafx.stage.Window} that the user will drag to move
	 *               the given {@link Stage}.
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
	 * @param node The {@link Node} that will be used to move the WindowManager.
	 */
	public static void setPaneDraggableByNode(final Node node, final Stage stage) {
		ChatRoomFXTools.setPaneDraggableByNode(node, stage);
	}

	/**
	 * Spawns a floating piece of text that flies upwards a little then disappears.
	 * The source point of the text is specified via the {@code x} and {@code y}
	 * parameters.
	 *
	 * @param text  The text to render.
	 * @param color The color of the rendered text.
	 * @param x     The starting x position of the text.
	 * @param y     The starting y position of the text.
	 */
	public static void spawnLabel(final String text, final Color color, final double x, final double y,
			final Stage stage) {
		ChatRoomFXTools.spawnLabel(text, color, x, y, stage);
	}

	public static void spawnLabelAtMousePos(final String text, final Color color, final Stage stage) {
		ChatRoomFXTools.spawnLabelAtMousePos(text, color, stage);
	}

	public static void styleBasicInput(final Region... inputs) {
		UnnamedFXTools.styleBasicInput(inputs);
	}

	private FXTools() {
	}

	public static void styleBasicInput(Paint borderColor, Paint activatedBorderColor, Region... inputs) {
		for (final Region r : inputs) {
			r.setBackground(UnnamedFXTools
					.getBackgroundFromColor(UnnamedFXTools.DEFAULT_WINDOW_COLOR.interpolate(Color.BLACK, 0.25)));
			r.setBorder(UnnamedFXTools.getBorderFromColor(borderColor));
			r.getStylesheets().add("branch/alixia/kröw/unnamed/tools/basic-input.css");
			r.getStyleClass().add("basic-input");

			(r instanceof Button ? ((Button) r).armedProperty() : r.focusedProperty())
					.addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> r.setBorder(
							UnnamedFXTools.getBorderFromColor(newValue ? activatedBorderColor : borderColor)));

		}
	}

	private static Paint[] inputBorderColors, inputActivatedColors;

	public static void setInputBorderColors(Paint[] inputBorderColors) {
		if (inputBorderColors != null && inputBorderColors.length == 0)
			return;
		FXTools.inputBorderColors = inputBorderColors;
	}

	public static void setInputActivatedColors(Paint[] inputActivatedColors) {
		if (inputActivatedColors != null && inputActivatedColors.length == 0)
			return;
		FXTools.inputActivatedColors = inputActivatedColors;
	}

	public static void styleInputs(Region... inputs) {
		styleInputs(-1, inputs);
	}

	public static void styleInputs(double fontSize, Region... inputs) {

		if (inputBorderColors == null && inputActivatedColors == null)
			styleBasicInput(inputs);
		else {
			for (Region r : inputs) {
				if (r instanceof Labeled) {
					Labeled labeled = (Labeled) r;
					labeled.setTextFill(Color.WHITE);
					labeled.setFont(Font.font("Courier", FontWeight.BOLD, fontSize));
				}
				r.setBackground(FXTools.getBackgroundFromColor(DEFAULT_WINDOW_COLOR.interpolate(Color.BLACK, 0.25)));
				r.setBorder(FXTools.getBorderFromColor(inputBorderColors == null ? Color.BLACK
						: inputBorderColors[(int) (Math.random() * inputBorderColors.length)]));
				(r instanceof Button ? ((Button) r).armedProperty()
						: r.focusedProperty())
								.addListener(
										(ChangeListener<Boolean>) (observable, oldValue,
												newValue) -> r
														.setBorder(
																UnnamedFXTools.getBorderFromColor(newValue
																		? inputActivatedColors == null ? Color.BLACK
																				: inputActivatedColors[(int) (Math
																						.random()
																						* inputActivatedColors.length)]
																		: inputBorderColors == null ? Color.BLACK
																				: inputBorderColors[(int) (Math.random()
																						* inputBorderColors.length)])));
			}
		}

	}

	private static final Object TABLE_COLUMN_RELATIVE_WIDTH_KEY = new Object(),
			TABLE_VIEW_TOTAL_COLUMN_PROPORTION_KEY = new Object();

	public static void sizeTableViewColumns(TableView<?> tableView) {

		if (tableView.getProperties().containsKey(TABLE_VIEW_TOTAL_COLUMN_PROPORTION_KEY))
			throw new IllegalArgumentException();

		int count = tableView.getColumns().size();
		DoubleProperty totalProportion = new SimpleDoubleProperty();
		tableView.getProperties().put(TABLE_VIEW_TOTAL_COLUMN_PROPORTION_KEY, totalProportion);
		for (TableColumn<?, ?> tc : tableView.getColumns()) {
			double value = tc.getProperties().containsKey(TABLE_COLUMN_RELATIVE_WIDTH_KEY)
					? (double) tc.getProperties().get(TABLE_COLUMN_RELATIVE_WIDTH_KEY)
					: 1d / count;

			// TODO Fix
			totalProportion.set(totalProportion.get() + value);
			tc.prefWidthProperty().bind(tableView.widthProperty().divide(totalProportion).multiply(value));
		}
	}

	public static void sizeTableViewColumns(TableView<?> tableView, NumberExpression totalSize) {

		if (tableView.getProperties().containsKey(TABLE_VIEW_TOTAL_COLUMN_PROPORTION_KEY))
			throw new IllegalArgumentException();

		int count = tableView.getColumns().size();
		DoubleProperty totalProportion = new SimpleDoubleProperty();
		tableView.getProperties().put(TABLE_VIEW_TOTAL_COLUMN_PROPORTION_KEY, totalProportion);
		for (TableColumn<?, ?> tc : tableView.getColumns()) {
			double value = tc.getProperties().containsKey(TABLE_COLUMN_RELATIVE_WIDTH_KEY)
					? (double) tc.getProperties().get(TABLE_COLUMN_RELATIVE_WIDTH_KEY)
					: 1d / count;

			// TODO Fix
			totalProportion.set(totalProportion.get() + value);
			tc.prefWidthProperty().bind(totalSize.divide(totalProportion).multiply(value));
		}
	}

	public static void setTableColumnRelativeWidth(double portion, TableColumn<?, ?> column) {
		column.getProperties().put(TABLE_COLUMN_RELATIVE_WIDTH_KEY, portion);
	}

}
