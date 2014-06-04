/*
 * Copyright 2014 London Knowledge Lab, Institute of Education.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 * List to which a filter and comparator can be applied.
 * @version $Revision$
 * @param <E> the generic type of the contents of the underlying list
 * @author Bernard Horan
 */
//$Date$
public class FilteredList<E> implements List<E> { 
    private static final Logger LOGGER = Logger.getLogger(FilteredList.class.getName());

    /**
     * Property name to indicate when the filter changes
     */
    public static final String PROP_FILTER = "filter";
    /**
     * Property name to indicate when the comparator changes
     */
    public static final String PROP_COMPARATOR = "comparator";
    
    private List<E> underlyingList;
    private List<E> filteredList;
    private Filter<E> filter;
    private Comparator<E> listComparator;
    private final transient PropertyChangeSupport propertySupport; 
    
    
    /**
     * Constructor, which takes an underlying list which is to be filtered 
     * (and sorted)
     * @param innerList a list of the same type as the FilteredList
     */
    public FilteredList(List<E> innerList) {
	propertySupport = new PropertyChangeSupport(this);
	underlyingList = innerList;
	//Catch all filter
	filter = new Filter<E>() {

	    @Override
	    public boolean isMatched(Object object) {
		return true;
	    }
	};
        //Aplha order
	listComparator = new Comparator<E>() {

	    @Override
	    public int compare(E t, E t1) {
		return t.toString().compareTo(t1.toString());
	    }
	};
	applyFilter();
    }
    
    
    /**
     * Set the filter for the filtered list, causing filter to be applied.
     * This will fire a property change.
     * @see #PROP_FILTER
     * @param aFilter the filter to be applied to the underlying list, 
     * which must have the same generic type parameter as the underlying list.
     */
    public void setFilter(Filter<E> aFilter) {
	Filter<E> oldValue = filter;
	this.filter = aFilter;
	applyFilter();
	propertySupport.firePropertyChange(PROP_FILTER, oldValue, filter);
    }
    
    /**
     * Set the comparator for the filtered list, causing the comparator to be applied.
     * This will cause a property change.
     * @see #PROP_COMPARATOR
     * @param aComparator a comparator to be applied to the underlying list, 
     * which must have the same generic type parameter as the underlying list 
     */
    public void setComparator(Comparator<E> aComparator) {
	Comparator<E> oldValue = listComparator;
	this.listComparator = aComparator;
	applyComparator();
	propertySupport.firePropertyChange(PROP_COMPARATOR, oldValue, filter);
    }
    
    
    private void applyFilter() {
	filteredList = new ArrayList<>();
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
    
    /**
     * Add a property change listener
     * @param listener the listener to be added
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
	propertySupport.addPropertyChangeListener(listener);
    }
    
    /**
     * Remove a property change listener
     * @param listener the listener to be removed
     */
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
    
    /**
     * Simple filter implementation
     * @param <E> must be of the same type as the underlying list
     */
    @SuppressWarnings("PublicInnerClass")
    public interface Filter<E> {
	/**
         * Return true if the parameter matches the rules of the filter
         * @param object the object to be matched
         * @return true if the object matches the filter rule
         */
        public boolean isMatched(E object);
    }
    
    

}
