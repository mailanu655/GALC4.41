package com.honda.galc.client.ui.component;

public abstract class TableModelFilter<T> {

	/**
	 * Returns true if the specified item should be shown;
	 * returns false if the item should be hidden.
	 */
	public abstract boolean include(T item);

}
