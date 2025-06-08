package com.honda.galc.client.datacollection.observer;

import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessTorque;
/**
 * 
 * <h3>IPsetTorqueDeviceObserver</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> IPsetTorqueDeviceObserver description </p>
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
 * @author Paul Chou
 * Apr 21, 2010
 *
 */
@ObserverInterface
public interface IPsetTorqueDeviceObserver {
	@ProcessTorqueState(actions = { Action.INIT, Action.REJECT })
	public void enableDevice(ProcessTorque torque);
	
	@ProcessTorqueState(actions = {Action.ABORT, Action.REJECT, Action.CANCEL, Action.COMPLETE, Action.SKIP_PART, Action.SKIP_PRODUCT })
	@ProcessPartState(actions = {Action.SKIP_PART, Action.SKIP_PRODUCT})
	public void disableDevice(DataCollectionState torque);
	
	@ProcessProductState(actions = { Action.COMPLETE })
	public void sendStatus(ProcessProduct state);

	@ProcessPartState(actions = {  Action.CANCEL, Action.REPAIR })
	@ProcessTorqueState(actions = {  Action.CANCEL, Action.REPAIR  })
	@ProcessProductState(actions = { Action.COMPLETE, Action.CANCEL })
	public void clearScanlessToolStatus(DataCollectionState state);
}
