
package com.beem.project.beem.utils;

import java.util.List;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;

/**
 * This class add a sort by insertion to a List.
 * All methods which allow you to insert an object at a specific index
 * will throw an UnsupportedOperationException.
 *
 * @author Da Risk <da_risk@beem-project.com>
 * @param <E> the type of elements maintained by this list
 */
public class SortedList<E> implements List<E> {

    private final List<E> mBackend;
    private final Comparator<? super E> mComparator;

    /**
     * Create a SortedList. The existing elements will be sorted.
     *
     * @param list list to sort
     * @param mComparator mComparator to use.
     */
    public SortedList(final List<E> list, final Comparator<? super E> mComparator) {
	this.mComparator = mComparator;
	this.mBackend = list;
	Collections.sort(mBackend, mComparator);
    }

    public int size() {
	return mBackend.size();
    }

    public boolean isEmpty() {
	return mBackend.isEmpty();
    }

    public boolean contains(Object o) {
	return mBackend.contains(o);
    }

    public Iterator<E> iterator() {
	return new SortedListIterator<E>(mBackend.listIterator());
    }

    public Object[] toArray() {
	return mBackend.toArray();
    }

    public <T> T[] toArray(T[] a) {
	return mBackend.toArray(a);
    }

    public boolean add(E e) {
	for (ListIterator<E> it = mBackend.listIterator(); it.hasNext();) {
	    if (mComparator.compare(e, it.next()) < 0) {
		if (it.hasPrevious()) {
		    it.previous();
		}
		it.add(e);
		return true;
	    }
	}
	mBackend.add(e);
	return true;
    }

    public boolean remove(Object o) {
	return mBackend.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
	return mBackend.containsAll(c);
    }

    public boolean addAll(Collection<? extends E> c) {
	boolean result = false;
	for (E e : c) {
	    boolean t = add(e);
	    if (t) {
		result = t;
	    }
	}
	return result;
    }

    /**
     * Add all the elements in the specified collection.
     * The index param is ignored.
     *
     * @param index ignored
     * @param c collection containing elements to be added to this list
     * @return true if this list changed as a result of the call
     */
    public boolean addAll(int index, Collection<? extends E> c) {
	return addAll(c);
    }

    /**
     * Add all the elements in the specified collection.
     * The index param is ignored.
     *
     * @param l collection containing elements to be added to this list
     * @return true if this list changed as a result of the call
     * @see addAll(Collection)
     */
    public boolean addAll(SortedList<? extends E> l) {
	if (!l.isEmpty()) {
	    if (mBackend.isEmpty()) {
		return mBackend.addAll(l);
	    }
	    boolean result = false;
	    E myfirst = mBackend.get(0);
	    E last = l.get(l.size() - 1);
	    E mylast = mBackend.get(mBackend.size() - 1);
	    E first = l.get(0);
	    if (mComparator.compare(last, myfirst) < 0) {
		result = mBackend.addAll(0, l);
	    } else if (mComparator.compare(first, mylast) > 0) {
		result = mBackend.addAll(l);
	    } else {
		Collection<? extends E> c = l;
		result = addAll(c);
	    }
	    return result;
	}
	return false;
    }

    public boolean removeAll(Collection<?> c) {
	return mBackend.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
	return mBackend.retainAll(c);
    }

    public void clear() {
	mBackend.clear();
    }

    @Override
    public boolean equals(Object o) {
	return mBackend.equals(o);
    }

    @Override
    public int hashCode() {
	return mBackend.hashCode();
    }

    public E get(int index) {
	return mBackend.get(index);
    }

    public E set(int index, E element) {
	throw new UnsupportedOperationException("set() is not supported in SortedList");
    }

    public void add(int index, E element) {
	throw new UnsupportedOperationException("add at specific index is not supported in SortedList");
    }

    public E remove(int index) {
	return mBackend.remove(index);
    }

    public int indexOf(Object o) {
	return mBackend.indexOf(o);
    }

    public int lastIndexOf(Object o) {
	return mBackend.lastIndexOf(o);
    }

    public ListIterator<E> listIterator() {
	return new SortedListIterator<E>(mBackend.listIterator());
    }

    public ListIterator<E> listIterator(int index) {
	return new SortedListIterator<E>(mBackend.listIterator(index));
    }

    public List<E> subList(int fromIndex, int toIndex) {
	return mBackend.subList(fromIndex, toIndex);
    }

    @Override
    public String toString() {
	return mBackend.toString();
    }

    /**
     * A SortedList.iterator don't allow list modification.
     * It use the mBackend iterator for the other operations.
     */
    private class SortedListIterator<E> implements ListIterator<E> {

	private ListIterator<E> mIt;

	/**
	 * Construct SortedList.Iterator.
	 *
	 * @param iterator the iterator of the backend list
	 */
	SortedListIterator(final ListIterator<E> iterator) {
	    mIt = iterator;
	}

	public void add(E e) {
	    throw new UnsupportedOperationException("add() not supported in SortedList iterator");
	}

	public boolean hasNext() {
	    return mIt.hasNext();
	}

	public E next() {
	    return mIt.next();
	}

	public boolean hasPrevious() {
	    return mIt.hasPrevious();
	}

	public E previous() {
	    return mIt.previous();
	}

	public int nextIndex() {
	    return mIt.nextIndex();
	}

	public int previousIndex() {
	    return mIt.previousIndex();
	}

	public void remove() {
	    mIt.remove();
	}

	public void set(E e) {
	    throw new UnsupportedOperationException("set () not supported in SortedList iterator");
	}
    }
}
