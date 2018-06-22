package kröw.math.lexer;

import java.util.HashMap;
import java.util.Map;

import kröw.math.lexer.exceptions.NotAWrapperException;

public final class MathChars {

	private static final Map<Character, Character> wrapperMap = new HashMap<>();
	static {
		addPairToWrapperMap('(', ')');
		addPairToWrapperMap('[', ']');
		addPairToWrapperMap('<', '>');
		addPairToWrapperMap('{', '}');
	}

	private static void addPairToWrapperMap(final char a, final char b) {
		wrapperMap.put(a, b);
		wrapperMap.put(b, a);
	}

	public static final Function getFunction(final String name) {
		for (final Function f : Function.functions)
			if (name.equalsIgnoreCase(f.getName()))
				return f;
		return null;
	}

	public static Operator getOperator(final String operator) {
		if (operator.isEmpty())
			return Operator.MULTIPLY;
		for (final Operator o : Operator.operators)
			if (operator.equalsIgnoreCase(o.operator))
				return o;
		return null;
	}

	public static char getWrapperPair(final char wrapperChar) {
		if (!isWrapper(wrapperChar))
			throw new NotAWrapperException();
		return wrapperMap.get(wrapperChar);
	}

	public static final boolean isAlphabetic(final char c) {
		return Character.isLetter(c);
	}

	public static boolean isCloseWrapper(final char c) {
		return c == ')' || c == ']' || c == '>' || c == '}';
	}

	public static final boolean isNumber(final char c) {
		return Character.isDigit(c) || c == '.';
	}

	public static boolean isOpenWrapper(final char c) {
		return c == '(' || c == '[' || c == '<' || c == '{';
	}

	public static boolean isWhitespace(final char c) {
		return Character.isWhitespace(c);
	}

	public static boolean isWrapper(final char c) {
		return isOpenWrapper(c) || isCloseWrapper(c);
	}

	public static final boolean possibleFunction(final String chars) {
		for (final Function f : Function.functions)
			if (f.getName().startsWith(chars))
				return true;
		return false;
	}

	public static final boolean possibleOperator(final String chars) {
		for (final Operator o : Operator.operators)
			// We must explicitly detect if chars is empty because the
			// EquationParser was coded expecting this method to stick up to
			// it's name; see method documentation.
			if (o.operator.startsWith(chars))
				return true;
		return false;
	}

	public static final boolean validFunctionChar(final char c) {
		return Character.isLetter(c) || c == '_';
	}

	private MathChars() {
	}
}
