package com.honda.galc.client.datacollection.observer;

import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.ProcessPart;

@ObserverInterface
public interface IRuleDevice {
	@ProcessPartState(actions = { Action.COMPLETE })
	void sendRuleStatus(final ProcessPart state);
}

