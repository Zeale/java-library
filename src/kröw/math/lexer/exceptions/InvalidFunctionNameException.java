package kröw.math.lexer.exceptions;

public class InvalidFunctionNameException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public InvalidFunctionNameException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InvalidFunctionNameException(final String term) {
		super("The character after \"" + term + "\" made the function name invalid.");
		// TODO Auto-generated constructor stub
	}

}
