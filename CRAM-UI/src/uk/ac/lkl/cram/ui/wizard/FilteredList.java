package uk.ac.lkl.cram.ui.wizard;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

/**
 * $Date$
 * $Revision$
 * @param <E> 
 * @author Bernard Horan
 */
public class FilteredList<E> implements List<E> {
    private static final Logger LOGGER = Logger.getLogger(FilteredList.class.getName());

    public static final String PROP_FILTER = "filter";
    public static final String PROP_COMPARATOR = "comparator";
    
    private List<E> underlyingList;
    private List<E> filteredList;
    private Filter filter;
    private Comparator<E> listComparator;
    private PropertyChangeSupport propertySupport;
    
    
    public FilteredList(List<E> innerList) {
	propertySupport = new PropertyChangeSupport(this);
	underlyingList = innerList;
	//Catch all filter
	filter = new Filter() {

	    @Override
	    public boolean isMatched(Object object) {
		return true;
	    }
	};
	listComparator = new Comparator<E>() {

	    @Override
	    public int compare(E t, E t1) {
		return t.toString().compareTo(t1.toString());
	    }
	};
	applyFilter();
    }
    
    
    public void setFilter(Filter aFilter) {
	Filter oldValue = filter;
	this.filter = aFilter;
	applyFilter();
	propertySupport.firePropertyChange(PROP_FILTER, oldValue, filter);
    }
    
    public void setComparator(Comparator aComparator) {
	Comparator oldValue = listComparator;
	this.listComparator = aComparator;
	applyComparator();
	propertySupport.firePropertyChange(PROP_COMPARATOR, oldValue, filter);
    }
    
    
    private void applyFilter() {
	filteredList = new ArrayList<E>();
	for (E object : underlyingList) {
	    if (filter.isMatched(object)) {
		filteredList.add(object);
	    }
	}
	applyComparator();
    }
    
    private void applyComparator() {
	Collections.sort(filteredList, listComparator);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
	propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
	propertySupport.removePropertyChangeListener(listener);
    }
    
    

    @Override
    public int size() {
	return filteredList.size();
    }

    @Override
    public boolean isEmpty() {
	return filteredList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
	return filteredList.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
	return filteredList.iterator();
    }

    @Override
    public Object[] toArray() {
	return filteredList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
	return filteredList.toArray(ts);
    }

    @Override
    public boolean add(E e) {
	throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
	throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> clctn) {
	return filteredList.containsAll(clctn);
    }

    @Override
    public boolean addAll(Collection<? extends E> clctn) {
	throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int i, Collection<? extends E> clctn) {
	throw new UnsupportedOperationException(); 
    }

    @Override
    public boolean removeAll(Collection<?> clctn) {
	throw new UnsupportedOperationException(); 
    }

    @Override
    public boolean retainAll(Collection<?> clctn) {
	throw new UnsupportedOperationException(); 
    }

    @Override
    public void clear() {
	throw new UnsupportedOperationException(); 
    }

    @Override
    public E get(int i) {
	return filteredList.get(i);
    }

    @Override
    public E set(int i, E e) {
	throw new UnsupportedOperationException(); 
    }

    @Override
    public void add(int i, E e) {
	throw new UnsupportedOperationException();
    }

    @Override
    public E remove(int i) {
	throw new UnsupportedOperationException(); 
    }

    @Override
    public int indexOf(Object o) {
	return filteredList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
	return filteredList.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
	return filteredList.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int i) {
	return filteredList.listIterator(i);
    }

    @Override
    public List<E> subList(int i, int i1) {
	return filteredList.subList(i, i1);
    }
    
    public interface Filter<E> {
	public boolean isMatched(E object);
    }
    
    

}
