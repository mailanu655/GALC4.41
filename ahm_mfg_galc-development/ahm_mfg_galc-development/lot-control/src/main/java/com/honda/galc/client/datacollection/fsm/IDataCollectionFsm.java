package com.honda.galc.client.datacollection.fsm;

import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessRefresh;
import com.honda.galc.client.datacollection.state.ProcessTorque;

/**
 * <h3>IDataCollectionFsm</h3>
 * <h4>
 * Gerneral defination of the finite sate machine for lot control rule based 
 * data collection.
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

@IFiniteStateMachine(states = {ProcessProduct.class, ProcessPart.class, ProcessTorque.class, ProcessRefresh.class}, initialState = ProcessProduct.class, finalState = ProcessRefresh.class)
public interface IDataCollectionFsm 
	extends IDataCollectionControl, IProductIdVerificationEvent, IPartVerificationEvent, INotificationEvent,
			ITorqueDataCollectionEvent, IUserControlEvent {
	void init();
}
