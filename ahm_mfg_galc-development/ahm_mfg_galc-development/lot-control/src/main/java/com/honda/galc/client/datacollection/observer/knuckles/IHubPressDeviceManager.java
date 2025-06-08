package com.honda.galc.client.datacollection.observer.knuckles;

import com.honda.galc.client.datacollection.observer.ObserverInterface;
import com.honda.galc.client.datacollection.observer.ProcessProductState;
import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.ProcessProduct;


@ObserverInterface
public interface IHubPressDeviceManager {
	@ProcessProductState(actions = {Action.OK})
	public void sendPressEnable(ProcessProduct state);
	
	@ProcessProductState(actions = {Action.COMPLETE})
	public void sendStatus(ProcessProduct state);
	
}
