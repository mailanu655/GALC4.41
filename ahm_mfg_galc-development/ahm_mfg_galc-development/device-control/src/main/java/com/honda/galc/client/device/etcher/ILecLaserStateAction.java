package com.honda.galc.client.device.etcher;

/**
 * @author Subu Kathiresan
 * @date May 25, 2017
 */
public interface ILecLaserStateAction {

	public void init();
	public void processMessage(LecLaserResponse response);
	public void complete();
	public LecLaserState getState();
	public void setStateMachine(LecLaserStateMachine stateMachine);
}
