package com.honda.galc.client.device.etcher;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.net.MessageHandler;

/**
 * @author Subu Kathiresan
 * @date May 19, 2017
 */
public class LecLaserStateMachine extends MessageHandler<String> {
	
	private volatile ILecLaserStateAction stateAction = null;
	private LecLaserSocketDevice device = null;

	public LecLaserStateMachine(LecLaserSocketDevice device) {
		this.device = device;
	}
	
	@Override
	public void processItem(String msg) {
		LecLaserResponse response = LecLaserResponse.get(Integer.parseInt(msg.trim()));
		getStateAction().processMessage(response);
	}

	public void transition() {
		int stateOrdinal = getStateAction().getState().ordinal();
		if (stateOrdinal < LecLaserState.values().length-1) {
			// transition to next state
			setStateAction(LecLaserState.values()[stateOrdinal + 1].getActionInstance());
			getStateAction().setStateMachine(this);
			getStateAction().init();
		}
	}
	
	public ILecLaserStateAction getStateAction() {
		if (stateAction == null) {
			// start fsm with the first state in the list
			stateAction = LecLaserState.values()[0].getActionInstance();
			stateAction.setStateMachine(this);
		}
		return stateAction;
	}
	
	public void setStateAction(ILecLaserStateAction stateAction) {
		this.stateAction = stateAction;
	}
	
	public void begin() {
		reset();
		getStateAction().init();
	}
	
	public void reset() {
		stateAction = null;
	}
	
	public String[] getCommands() {
		return device.getCommands();
	}
	
	public LecLaserSocketDevice getDevice() {
		return device;
	}

	public void setDevice(LecLaserSocketDevice device) {
		this.device = device;
	}
	
	public Logger getLogger() {
		return device.getLogger();
	}
}
