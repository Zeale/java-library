package org.alixia.javalibrary.json;

public class JSONNumber extends Number implements JSONValue {

	/**
	 * SUID
	 */
	private static final long serialVersionUID = 1L;
	private final boolean neg;
	private final String left, right, exp;

	public JSONNumber(boolean neg, String left, String right, String exp) {
		this.neg = neg;
		this.left = left;
		this.right = right;
		this.exp = exp;
	}

	@Override
	public double doubleValue() {
		StringBuilder b = new StringBuilder(left);
		if (right != null)
			b.append('.').append(right);
		if (exp != null)
			b.append('e').append(exp);
		return (neg ? -1 : 1) * Double.parseDouble(b.toString());
	}

	@Override
	public float floatValue() {
		return (float) doubleValue();
	}

	@Override
	public int intValue() {
		int val = Integer.parseInt(left);
		if (neg)
			val = -val;
		if (exp != null)
			val = (int) Math.pow(10, val);
		return val;
	}

	@Override
	public long longValue() {
		long val = Long.parseLong(left);
		if (neg)
			val = -val;
		if (exp != null)
			val = (long) Math.pow(10, val);
		return val;
	}

	@Override
	public String toString() {
		return (neg ? "-" : "") + left + '.' + right + 'E' + exp;
	}

	@Override
	public String toString(String indentation) {
		return toString();
	}

}
