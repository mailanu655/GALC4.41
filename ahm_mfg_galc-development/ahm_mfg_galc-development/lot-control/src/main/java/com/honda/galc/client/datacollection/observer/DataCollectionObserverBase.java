package com.honda.galc.client.datacollection.observer;

import com.honda.galc.client.common.IObserver;
import com.honda.galc.client.common.Observable;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.state.DataCollectionState;

/**
 * <h3>DataCollectionObserverBase</h3>
 * <h4>
 * Data Collection Observer base class.
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Aug.19, 2009</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Paul Chou
 */
public abstract class DataCollectionObserverBase implements IObserver {
	
	public void update(Observable o, Object arg) {
		DataCollectionObserverInvoker.invoke(this, o, arg);
	}
	
	public DataCollectionState getCurrentState(String applicationId){
		return DataCollectionController.getInstance(applicationId).getState();
	}

	public void cleanUp() {
		// TODO Auto-generated method stub
		
	}

}
