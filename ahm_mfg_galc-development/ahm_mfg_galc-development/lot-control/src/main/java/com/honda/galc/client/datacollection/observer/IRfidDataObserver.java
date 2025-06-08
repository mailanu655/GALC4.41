package com.honda.galc.client.datacollection.observer;

import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.DataCollectionState;

@ObserverInterface
public interface IRfidDataObserver {
	
	
	@ProcessProductState(actions = {Action.CANCEL})
	@ProcessPartState(actions = {Action.CANCEL})
	@ProcessTorqueState(actions = {Action.CANCEL})
	@ProcessRefreshState(actions= {Action.COMPLETE})
	public void populateProductId(DataCollectionState state);

	@ProcessProductState(actions = {Action.COMPLETE,Action.SKIP_PRODUCT})
	@ProcessRefreshState(actions= {Action.COMPLETE})
	public void stopTimer(DataCollectionState state);
}
