package kr�w.math.lexer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Equation extends ArrayList<Term> {

	/**
	 * SUID
	 */
	private static final long serialVersionUID = 1L;

	public Equation() {
		super();
	}

	public Equation(final Collection<? extends Term> c) {
		super(c);
	}

	public Equation(final int initialCapacity) {
		super(initialCapacity);
	}

	public double evaluate() {
		// TODO if(isEmpty())throw new EmptyEquationException();
		if (size() == 1)
			return get(0).value;

		final ArrayList<Integer> list = new ArrayList<>();

		for (final Term t : this)
			if (!list.contains(t.getOperator().precedence))
				list.add(t.getOperator().precedence);
		list.sort(Collections.reverseOrder());// This makes the list have greater values first and smaller values after.

		final Equation storage = new Equation(this);
		for (final int precedence : list) {// i decreases as we evaluate because of the list's sorting.
			System.out.println("PRECEDENCE: " + precedence);
			if (precedence == Operator.END_LINE.precedence)
				return storage.get(0).value;
			for (int pos = 0; pos < storage.size(); pos++) {
				System.out.println("TERM: " + storage.get(pos).value + storage.get(pos).getOperator().operator);
				final Operator op = storage.get(pos).getOperator();

				if (op.precedence == precedence) {
					storage.set(pos, new Term(op.operate(storage.get(pos).value, storage.get(pos + 1).value),
							storage.get(pos + 1).getOperator()));
					storage.remove(pos + 1);
					pos--;
				}
			}
		}

		return storage.get(0).value;

	}

}
