package com.honda.galc.client.datacollection.observer;

import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.ProcessPart;

@ObserverInterface
public interface IPartInstallObserver {
	@ProcessPartState(actions = {Action.INIT})
	public void partInstall(final ProcessPart part);
	
	@ProcessProductState(actions = {Action.SKIP_PRODUCT})
	@ProcessPartState(actions = {Action.CANCEL, Action.SKIP_PRODUCT})
	public void handleDataCollectionCancel(final ProcessPart part);
}
