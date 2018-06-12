package kröw.math.lexer;

import java.util.ArrayList;
import java.util.List;

//TODO Make these, along with operators, be based off of a file.
public class Function {

	private interface FunctionImpl {
		// This may later change to taking a varargs of Objs and returning an Object.
		double evaluate(double input);
	}

	static List<Function> functions = new ArrayList<>();

	public static final Function COSINE = new Function("cos", Math::cos);

	public static final Function SINE = new Function("sin", Math::sin);
	{
		functions.add(this);
	}

	private final FunctionImpl impl;
	private final String name;

	public Function(final String name, final FunctionImpl impl) {
		this.name = name;
		this.impl = impl;
	}

	public double calculate(final double input) {
		return impl.evaluate(input);
	}

	// private boolean caseSensitive;

	public String getName() {
		return name;
	}

}
