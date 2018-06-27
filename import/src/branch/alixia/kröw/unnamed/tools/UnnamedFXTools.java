package branch.alixia.kröw.unnamed.tools;

import static javafx.scene.paint.Color.BLUE;
import static javafx.scene.paint.Color.GOLD;
import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.RED;

import java.util.LinkedList;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import main.alixia.javalibrary.javafx.tools.FXTools;

public final class UnnamedFXTools {

	public static final Color DEFAULT_WINDOW_COLOR = new Color(0.34, 0.34, 0.34, 1);
	public static final Color ITEM_BORDER_COLOR = Color.BLUE;
	public static final Color SECONDARY_WINDOW_BORDER_COLOR = ITEM_BORDER_COLOR.interpolate(DEFAULT_WINDOW_COLOR, 0.5);

	public static final double COMMON_BORDER_WIDTH = 2;
	private static final Color[] DEFAULT_COLORWHEEL_TRANSITION_COLORS = new Color[] { RED, GOLD, GREEN, BLUE };
	private static final Duration DEFAULT_COLORWHEEL_TRANSITION_DURATION = Duration.seconds(0.5);

	public static Transition applyHoverColorAnimation(final Shape shape) {
		return applyHoverColorAnimation(shape, DEFAULT_COLORWHEEL_TRANSITION_DURATION,
				DEFAULT_COLORWHEEL_TRANSITION_COLORS);
	}

	public static Transition applyHoverColorAnimation(final Shape shape, final Color... colors) {
		return applyHoverColorAnimation(shape, DEFAULT_COLORWHEEL_TRANSITION_DURATION, colors);
	}

	public static Transition applyHoverColorAnimation(final Shape shape, final Duration duration,
			final Color... colors) {

		final Transition repeater = buildColorwheelTransition(shape, duration, colors);

		shape.setOnMouseEntered(event -> repeater.play());
		shape.setOnMouseExited(event -> repeater.pause());

		return repeater;
	}

	public static Transition buildColorwheelTransition(final Shape shape) {
		return buildColorwheelTransition(shape, DEFAULT_COLORWHEEL_TRANSITION_COLORS);
	}

	public static Transition buildColorwheelTransition(final Shape shape, final Color... colors) {
		return buildColorwheelTransition(shape, DEFAULT_COLORWHEEL_TRANSITION_DURATION, colors);
	}

	public static Transition buildColorwheelTransition(final Shape shape, final Duration duration) {
		return buildColorwheelTransition(shape, duration, DEFAULT_COLORWHEEL_TRANSITION_COLORS);
	}

	public static Transition buildColorwheelTransition(final Shape shape, final Duration duration,
			final Color... colors) {
		shape.setFill(colors[0]);
		final FillTransition[] transitions = new FillTransition[colors.length];
		for (int i = 0; i < colors.length - 1;)
			transitions[i] = new FillTransition(duration, shape, colors[i], colors[++i]);

		transitions[transitions.length - 1] = new FillTransition(duration, shape, colors[colors.length - 1], colors[0]);

		final SequentialTransition repeater = new SequentialTransition(transitions);

		repeater.setCycleCount(Animation.INDEFINITE);
		return repeater;
	}

	public static Background getBackgroundFromColor(final Paint color) {
		return new Background(new BackgroundFill(color, null, null));
	}

	public static Background getBackgroundFromColor(final Paint color, final double radius) {
		return new Background(new BackgroundFill(color, radius < 0 ? null : new CornerRadii(radius), null));
	}

	public static Border getBorderFromColor(final Paint color) {
		return getBorderFromColor(color, 2);
	}

	public static Border getBorderFromColor(final Paint color, final double width) {
		return new Border(
				new BorderStroke(color, BorderStrokeStyle.SOLID, null, width < 0 ? null : new BorderWidths(width)));
	}

	public static Border getBorderFromColor(final Paint color, final double width, final double radii) {
		return new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, radii < 0 ? null : new CornerRadii(radii),
				width < 0 ? null : new BorderWidths(width)));
	}

	public static void setDefaultBackground(final Region item) {
		item.setBackground(getBackgroundFromColor(DEFAULT_WINDOW_COLOR));
		if (item instanceof ScrollPane) {
			item.getStylesheets().add("branch/alixia/kröw/unnamed/tools/default-background.css");
			item.getStyleClass().add("default-background");
		}
	}

	public static void styleBasicInput(final Region... inputs) {
		LinkedList<Button> buttons = new LinkedList<>();
		LinkedList<Region> others = new LinkedList<>();
		for (Region r : inputs)
			if (r instanceof Button)
				buttons.add((Button) r);
			else
				others.add(r);

		FXTools.styleBasicInput(ITEM_BORDER_COLOR, Color.RED, buttons.toArray(new Button[0]));
		FXTools.styleBasicInput(ITEM_BORDER_COLOR, Color.GREEN, others.toArray(new Region[0]));

	}

}
