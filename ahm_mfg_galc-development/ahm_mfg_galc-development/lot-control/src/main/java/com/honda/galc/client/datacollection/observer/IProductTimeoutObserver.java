package com.honda.galc.client.datacollection.observer;

import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.DataCollectionState;

@ObserverInterface
public interface IProductTimeoutObserver {
	
	@ProcessProductState(actions = { Action.INIT })
	public void checkNextExpectedProduct(DataCollectionState arg);
	
}
