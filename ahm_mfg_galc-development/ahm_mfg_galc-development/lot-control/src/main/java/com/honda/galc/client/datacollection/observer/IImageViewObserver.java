package com.honda.galc.client.datacollection.observer;

import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessTorque;

@ObserverInterface
public interface IImageViewObserver extends IViewObserver {
	@ProcessPartState(actions = {Action.REJECT})
	void rejectPart(ProcessPart state);
	
	@ProcessTorqueState(actions = {Action.REJECT})
	void rejectTorque(ProcessTorque state);
	
	@ProcessRefreshState(actions = {Action.REJECT})
	void rejectLastInput(DataCollectionState state);
}
