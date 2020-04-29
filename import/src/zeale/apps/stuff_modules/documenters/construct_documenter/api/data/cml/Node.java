package zeale.apps.stuff_modules.documenters.construct_documenter.api.data.cml;

import java.util.Collection;
import java.util.List;

import org.alixia.chatroom.api.QuickList;

public class Node {
	private static String escape(String in) {
		if (in == null)
			in = "";
		return in.replace("\\", "\\\\").replace("<", "\\o").replace(">", "\\c");
	}

	private String name, value;

	private QuickList<Node> children;

	public Node(final String name) {
		this(name, true);
	}

	Node(final String name, final boolean escape) {
		this.name = escape ? escape(name) : name;
	}

	Node(final String name, final boolean escape, final Collection<? extends Node> children) {
		this.name = escape ? escape(name) : name;
		this.children = new QuickList<>(children);
	}

	Node(final String name, final boolean escape, final Node... children) {
		this.name = escape ? escape(name) : name;
		this.children = new QuickList<>(children);
	}

	public Node(final String name, final Collection<? extends Node> nodes) {
		this(name, true, nodes);
	}

	public Node(final String name, final Node... nodes) {
		this(name, true, nodes);
	}

	public Node(final String name, final String value) {
		this(name, value, true);
	}

	Node(final String name, final String value, final boolean escape) {
		this.name = escape ? escape(name) : name;
		this.value = escape ? escape(value) : value;
	}

	public void clearChildren() {
		children = null;
	}

	public void clearValue() {
		value = null;
	}

	public List<Node> getChildren() {
		return children;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public boolean hasChildren() {
		return children != null;
	}

	public boolean hasValue() {
		return value != null;
	}

	/**
	 * Returns the name of this {@link Node} converted to being safe-to-write.
	 *
	 * @return the name of this {@link Node} (safe to write).
	 */
	String name() {
		return name;
	}

	void name(final String name) {
		this.name = name;
	}

	public void setChildren(final Collection<Node> nodes) {
		if (value != null)
			throw new RuntimeException("A node can't have both a value and children.");
		if (children == null)
			children = new QuickList<>(nodes);
		else
			children.addAll(nodes);
	}

	public void setChildren(final Node... children) {
		if (value != null)
			throw new RuntimeException("A node can't have both a value and children.");
		if (children == null)
			this.children = new QuickList<>(children);
		else
			this.children.addAll(children);
	}

	public void setName(final String name) {
		this.name = escape(name);
	}

	public void setValue(final String value) {
		if (children != null)
			throw new RuntimeException("A node cannot have both children and a value.");
		this.value = escape(value);
	}

	@Override
	public String toString() {
		return getName() + ": " + (hasValue() ? getValue() : hasChildren() ? getChildren() : "empty");
	}

	/**
	 * Returns the value of this {@link Node} converted to being safe-to-write.
	 *
	 * @return The value of this {@link Node} (safe to write).
	 */
	String value() {
		return value;
	}

	void value(final String value) {
		this.value = value;
	}

}
