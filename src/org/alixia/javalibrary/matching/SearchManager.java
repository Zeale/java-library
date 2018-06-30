package org.alixia.javalibrary.matching;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SearchManager<MT> {
	private final List<Matchable<MT>> fullList;
	private List<Matchable<MT>> showing = new LinkedList<>(), removed = new LinkedList<>();

	private final List<Matchable<MT>> transferList = new LinkedList<>();
	private MT currentFilter = null;

	/**
	 * <p>
	 * Any item that matches the specified filter will be moved to the list of shown
	 * items if it is not already there. Any item that does not match will be
	 * removed from the list of shown items, if it is there.
	 * <p>
	 * If <code>null</code> is given as the <code>filter</code> for this method,
	 * this {@link SearchManager} will reset. This means that the list of shown
	 * items will be cleared and every single item in this {@link SearchManager}
	 * will be matched against <code>null</code> to see if it should be shown.
	 * Sometimes, errors can occur (perhaps via a thread editing a list while it is
	 * being filtered by this {@link SearchManager}, even though this specific
	 * action is strictly prohibited). In such a case, filtering items with
	 * <code>null</code> will clear their matching or non-matching status, and that
	 * status will be re-evaluated.
	 * <p>
	 * Do note that in the case of <code>null</code> being entered to refresh this
	 * {@link SearchManager}, the items in this manager will still be filtered with
	 * <code>null</code>. Some applications that use {@link Matchable}s have their
	 * {@link Matchable#matches(Object)} method return <code>true</code> if it
	 * receives <code>null</code> in any case. This allows for empty search queries
	 * to show all possible results in a list, amongst other things.
	 * <p>
	 * If whatever object your {@link Matchable} compares itself against has an
	 * "empty" value (for example, the {@link String}, <code>""</code> is empty)
	 * then you may consider using it as a filter instead of <code>null</code>, as
	 * an object's empty value won't refresh the entire {@link SearchManager}.
	 * 
	 * @param filter The filter to use to filter items. If this is
	 *               <code>null</code>, then this search manager is reset.
	 */
	public synchronized void filter(MT filter) {
		currentFilter = filter;
		if (filter == null) {
			removed.clear();
			showing.clear();
			for (Matchable<MT> m : fullList)
				(m.matches(filter) ? showing : removed).add(m);
		} else {
			for (Iterator<Matchable<MT>> iterator = showing.iterator(); iterator.hasNext();) {
				Matchable<MT> m = iterator.next();
				if (!m.matches(filter)) {
					transferList.add(m);
					iterator.remove();
				}
			}
			for (Iterator<Matchable<MT>> iterator = removed.iterator(); iterator.hasNext();) {
				Matchable<MT> m = iterator.next();
				if (m.matches(filter)) {
					showing.add(m);
					iterator.remove();
				}
			}
			removed.addAll(transferList);
			transferList.clear();
		}

	}

	public void setShowingList(List<Matchable<MT>> showing) {
		if (showing == null)
			throw new IllegalArgumentException();
		this.showing = showing;
	}

	/**
	 * @param items A <b>COPY</b> of the current state of this specified list will
	 *              be used as a full list of items. If any items are to be added to
	 *              the search manager at any time, they should be added through the
	 *              {@link #addItem(Matchable)} method.
	 */
	public SearchManager(List<Matchable<MT>> items) {
		if (items == null)
			throw new IllegalArgumentException();
		fullList = new LinkedList<>(items);
	}

	public synchronized void addItem(Matchable<MT> item) {
		fullList.add(item);
		(item.matches(currentFilter) ? showing : removed).add(item);
	}

	public synchronized void removeItem(Matchable<MT> item) {
		fullList.remove(item);
		showing.remove(item);
		removed.remove(item);
	}

}
