package com.honda.galc.client.datacollection.observer;

import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessTorque;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>IDeviceObserver</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>Paul Chou</TD>
 * <TD>Sep 11, 2009</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Paul Chou
 */
@ObserverInterface
public interface IJobTorqueDeviceObserver {
	
	@ProcessTorqueState(actions = { Action.INIT })
	public void setJob(ProcessTorque torque);
	
	@ProcessTorqueState(actions = { Action.ABORT, Action.REJECT, Action.CANCEL, Action.COMPLETE, Action.SKIP_PART, Action.SKIP_PRODUCT })
	@ProcessPartState(actions = {Action.SKIP_PART, Action.SKIP_PRODUCT})
	public void abortJob(DataCollectionState torque);
	
	@ProcessProductState(actions = { Action.COMPLETE })
	public void sendStatus(ProcessProduct state);

}
