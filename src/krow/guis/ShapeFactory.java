package krow.guis;

import javafx.scene.shape.Circle;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;

public final class ShapeFactory {
	public static Circle buildCircle() {
		return buildCircle(50);
	}

	public static Circle buildCircle(final double size) {
		return new Circle(size / 2);
	}

	/**
	 * <p>
	 * Builds GitHub's Octicon.
	 * <p>
	 * Please note that the SVG code was copied straight from GitHub's website.
	 *
	 * @param scaleFactor
	 *            The scale factor of this {@link SVGPath}. This is applied to
	 *            both the {@link Shape#scaleXProperty()} and the
	 *            {@link Shape#scaleYProperty()} of the resulting
	 *            {@link SVGPath}.
	 * @return A {@link SVGPath} representing an Octicon.
	 */
	public static SVGPath buildOcticon(final double scaleFactor) {
		final SVGPath path = new SVGPath();
		path.setContent(
				"M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0 0 16 8c0-4.42-3.58-8-8-8z");
		path.setFillRule(FillRule.EVEN_ODD);
		path.setScaleX(scaleFactor);
		path.setScaleY(scaleFactor);
		path.setStrokeWidth(0.25);
		return path;
	}

	public static Shape buildRegularShape(double size, int sides) {
		sides = Math.abs(sides);
		size = size == 0 ? 100 : Math.abs(size);
		if (sides == Integer.MAX_VALUE)
			return buildCircle(size);

		final double[] points = new double[sides * 2];

		points[0] = 0;
		points[1] = -size / 2;

		final double turnInc = 180 - (sides - 2) * 180 / sides;
		double turningDeg = 0 + (sides & 1) * (turnInc / 2);

		final double distance =

				// Modified size code for shapes with even side count

				// (sides & 1) == 0 ? (2 * Math.tan(Math.toRadians(180 / sides))
				// * (size / 2)):
				2 * size / (1 / Math.tan(Math.toRadians(180 / sides)) + 1 / Math.sin(Math.toRadians(180 / sides)));

		for (int i = 2; i < points.length; i++) {
			points[i] = points[i++ - 2] + distance * Math.cos(Math.toRadians(turningDeg));
			points[i] = points[i - 2] + distance * Math.sin(Math.toRadians(turningDeg));
			turningDeg += turnInc;
		}

		final Polygon p = new Polygon(points);
		return p;
	}

	public static Polygon buildTriangle() {
		return buildTriangle(100);
	}

	public static Polygon buildTriangle(final double size) {
		return new Polygon(0, 0, -size / 2, size, size / 2, size);
	}

	private ShapeFactory() {
	}

}
