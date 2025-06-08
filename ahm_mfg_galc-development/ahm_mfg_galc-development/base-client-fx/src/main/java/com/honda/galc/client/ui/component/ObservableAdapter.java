package com.honda.galc.client.ui.component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * 
 * <h3>ObservableAdapter</h3>
 * <p>
 * This class provides a wrapper around a pojo class such as a JPA entity, to
 * enable changes to the object to trigger the change listener. and to trigger
 * change events.
 * </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * 
 * </TABLE>
 * 
 * @author Suriya Sena Feb 11, 2014 - JavaFx migration
 * 
 */

public final class ObservableAdapter<T> {

	private BooleanProperty property = new SimpleBooleanProperty();

	private final T value;

	public ObservableAdapter(T pojo) {
		this.value = pojo;
	}

	public T getValue() {
		return value;
	}

	public BooleanProperty getObservableProperty() {
		return property;
	}

	public void update() {
		property.set(!property.get()); /* update property to trigger change */
	}

}
