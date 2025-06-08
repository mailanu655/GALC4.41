package com.honda.galc.client.ui.component;

import javafx.beans.Observable;
import javafx.util.Callback;


/**
 * 
 * <h3>ObservableAdapterCallback Class description</h3>
 * <p> ObservableAdapterCallback description.  This class is used to trigger change on a ObservableList Item </p>
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
 * @author Suriya Sena
 * Feb 12 2014
 *
 */
public class ObservableAdapterCallback<T> implements Callback<ObservableAdapter<T>, Observable[]> {
	@Override
	public Observable[] call(ObservableAdapter<T> p) {
		Observable[] observables = new Observable[1];
		observables[0] = p.getObservableProperty();
		return observables;
	}
}