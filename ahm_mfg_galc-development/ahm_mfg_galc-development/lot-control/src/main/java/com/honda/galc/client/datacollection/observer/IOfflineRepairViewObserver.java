package com.honda.galc.client.datacollection.observer;

import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessRefresh;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.client.datacollection.view.IViewManager;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>IViewObserver</code> is ...
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
public interface IOfflineRepairViewObserver extends IViewObserver {
	
	//Product Id action mapping
	@ProcessProductState(actions = { Action.INIT})
	void initProductId(final ProcessProduct state);
	@ProcessProductState(actions = { Action.RECEIVED})
	void receivedProductId(final ProcessProduct state);
	@ProcessProductState(actions = { Action.OK })
	void productIdOk(final ProcessProduct state);
	@ProcessProductState(actions = { Action.NG })
	void productIdNg(final ProcessProduct state);
	@ProcessProductState(actions = { Action.COMPLETE})
	void refreshRepairParts(ProcessProduct state);
	

	//Part verification action mapping
	@ProcessPartState(actions = { Action.INIT})
	void initPartSn(final ProcessPart state);
	@ProcessPartState(actions = { Action.OK})
	void partSnOk(final ProcessPart state);
	@ProcessPartState(actions = { Action.NG})
	void partSnNg(final ProcessPart state);
	@ProcessPartState(actions = { Action.RECEIVED})
	void receivedPartSn(final ProcessPart state);
	
	
	//Torque collection action mapping
	@ProcessTorqueState(actions = { Action.INIT})
	void initTorque(final ProcessTorque state);
	@ProcessTorqueState(actions = { Action.OK})
	void torqueOk(final ProcessTorque state);
	@ProcessTorqueState(actions = { Action.NG})
	void torqueNg(final ProcessTorque state);
	@ProcessTorqueState(actions = { Action.COMPLETE})
	void completeCollectTorques(final ProcessTorque state);

	//Refresh delay action mapping
	@ProcessRefreshState(actions = {Action.INIT})
	void initRefreshDelay(final ProcessRefresh state);
	
	// Common mapping for all states
	@ProcessPartState(actions = { Action.SKIP_PART})
	void skipPart(DataCollectionState state);
	@ProcessTorqueState(actions = { Action.SKIP_PART})
	void skipCurrentInput(final ProcessTorque state);
	@ProcessProductState(actions = { Action.SKIP_PRODUCT })
	@ProcessPartState(actions = { Action.SKIP_PRODUCT})
	@ProcessTorqueState(actions = { Action.SKIP_PRODUCT})
	void skipProduct(DataCollectionState state);
	@ProcessProductState(actions = { Action.CANCEL })
	@ProcessPartState(actions = { Action.CANCEL})
	@ProcessTorqueState(actions = { Action.CANCEL})
	void refreshScreen(DataCollectionState state);
	@ProcessProductState(actions = { Action.ERROR})
	@ProcessPartState(actions = { Action.ERROR})
	@ProcessTorqueState(actions = { Action.ERROR})
	void notifyError(DataCollectionState state);
	@ProcessProductState(actions = { Action.MESSAGE})
	@ProcessPartState(actions = { Action.MESSAGE})
	@ProcessTorqueState(actions = { Action.MESSAGE})
	void message(DataCollectionState state);
	
}
