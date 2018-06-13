package kröw.math.lexer.exceptions;

public class IllegalCharException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public IllegalCharException() {
	}

	public IllegalCharException(final String string) {
		super(string);
	}

}
