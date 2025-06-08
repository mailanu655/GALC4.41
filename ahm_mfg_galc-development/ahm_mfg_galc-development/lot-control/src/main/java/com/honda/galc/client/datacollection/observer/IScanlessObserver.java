package com.honda.galc.client.datacollection.observer;

import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.DataCollectionState;

@ObserverInterface
public interface IScanlessObserver {


	@ProcessProductState(actions = {Action.CANCEL,Action.COMPLETE})
	@ProcessPartState(actions = {Action.CANCEL})
	@ProcessTorqueState(actions = {Action.CANCEL})
	@ProcessRefreshState(actions= {Action.COMPLETE})
	public void clearScanlessMessages(DataCollectionState state);


}
