package com.honda.galc.client.device.etcher;

import com.honda.galc.client.device.etcher.state.AcquireHostControl;
import com.honda.galc.client.device.etcher.state.EtchInProgress;
import com.honda.galc.client.device.etcher.state.EtchCompleted;
import com.honda.galc.client.device.etcher.state.SendCommands;
import com.honda.galc.util.ReflectionUtils;

/**
 * @author Subu Kathiresan
 * @date May 18, 2017
 */
public enum LecLaserState {
	
	acquireHostControl		(AcquireHostControl.class),
	sendCommands			(SendCommands.class),
	etchInProgress			(EtchInProgress.class),
	etchCompleted			(EtchCompleted.class);
	
	private Class<? extends ILecLaserStateAction> stateClass;
	
	public void setStateClass(Class<? extends ILecLaserStateAction> stateClass) {
		this.stateClass = stateClass;
	}
	private LecLaserState(Class<? extends ILecLaserStateAction> stateClass) {
		this.setStateClass(stateClass);
	}

	public Class<? extends ILecLaserStateAction> getStateClass() {
		return stateClass;
	}

	public ILecLaserStateAction getActionInstance() {
		return ReflectionUtils.createInstance(stateClass);
	}
}
