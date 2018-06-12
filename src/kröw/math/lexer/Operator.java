package kröw.math.lexer;

import java.util.ArrayList;
import java.util.List;

/**
 * Temporarily empty class for development purposes.
 *
 * @author Zeale
 *
 */
public final class Operator implements Comparable<Operator> {

	private static interface OperatorImpl {
		double operate(double x, double y);
	}

	static List<Operator> operators = new ArrayList<>();

	public static final Operator ADD = new Operator("+", (x, y) -> x + y, 1);
	public static final Operator MULTIPLY = new Operator("*", (x, y) -> x * y, 2);
	public static final Operator SUBTRACT = new Operator("-", (x, y) -> x - y, 1);
	public static final Operator DIVIDE = new Operator("/", (x, y) -> x / y, 2);
	public static final Operator POWER = new Operator("^", (x, y) -> Math.pow(x, y), 3);
	public static final Operator MODULUS = new Operator("%", (x, y) -> x % y, 2);

	public static final Operator END_LINE = new Operator(";", null, -99999);

	{
		operators.add(this);
		operators.sort(null);
	}
	public final String operator;
	private final OperatorImpl impl;

	public final int precedence;

	private Operator(final String operator, final OperatorImpl impl, final int precedence) {
		this.operator = operator;
		this.impl = impl;
		this.precedence = precedence;
	}

	@Override
	public int compareTo(final Operator o) {
		// If o's precedence is greater, this method returns negative, meaning that this
		// operator is less than o.
		return precedence - o.precedence;
	}

	public double operate(final double x, final double y) {
		return impl.operate(x, y);
	}

}
