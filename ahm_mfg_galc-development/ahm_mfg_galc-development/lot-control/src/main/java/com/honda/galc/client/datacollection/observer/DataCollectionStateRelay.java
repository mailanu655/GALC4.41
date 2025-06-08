package com.honda.galc.client.datacollection.observer;

import org.bushe.swing.event.EventBus;

import com.honda.galc.client.common.IObserver;
import com.honda.galc.client.common.Observable;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.DataCollectionState;

/**
 * 
 * <h3>DataCollectionStateRelay</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DataCollectionStateRelay description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Apr 27, 2011</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Apr 27, 2011
 */

public class DataCollectionStateRelay implements IObserver{

	public DataCollectionStateRelay(ClientContext context) {
		super();
	}

	public void cleanUp() {
		// TODO Auto-generated method stub
		
	}

	public void update(Observable observable, Object data) {
		EventBus.publish(((DataCollectionState)data));
		
	}

}
