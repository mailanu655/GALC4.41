package com.honda.galc.client.datacollection.observer;

import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;

/**
 * 
 * <h3>IKnucklesDeviceObserver</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> IKnucklesDeviceObserver description </p>
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
 * Nov 25, 2010
 *
 */
@ObserverInterface
public interface IProductSubIdDeviceObserver {
	@ProcessPartState(actions = { Action.INIT})
	public void checkProductIdSide(final ProcessPart part);
	
	@ProcessProductState(actions = { Action.INIT})
	public void reset(final ProcessProduct product);
	
	@ProcessProductState(actions = { Action.CANCEL })
	@ProcessPartState(actions = { Action.CANCEL})
	@ProcessTorqueState(actions = { Action.CANCEL})
	void cancel(DataCollectionState state);
	
	@ProcessPartState(actions = { Action.SKIP_PART})
	void skipPart(DataCollectionState state);
	
	@ProcessPartState(actions = { Action.REJECT})
	void rejectPart(DataCollectionState state);

}
