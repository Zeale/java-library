package org.alixia.chatroom.api;

import java.util.Collection;
import java.util.LinkedList;

public class QuickList<E> extends LinkedList<E> {

	/**
	 * SUID
	 */
	private static final long serialVersionUID = 1L;

	public QuickList(final Collection<? extends E> c) {
		super(c);
	}

	@SafeVarargs
	public QuickList(final E... es) {
		for (final E e : es)
			add(e);
	}

}
