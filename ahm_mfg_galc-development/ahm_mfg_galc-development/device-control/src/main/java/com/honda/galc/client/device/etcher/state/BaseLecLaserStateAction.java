package com.honda.galc.client.device.etcher.state;

import com.honda.galc.client.device.etcher.ILecLaserDeviceListener;
import com.honda.galc.client.device.etcher.ILecLaserStateAction;
import com.honda.galc.client.device.etcher.LecLaserCommand;
import com.honda.galc.client.device.etcher.LecLaserResponse;
import com.honda.galc.client.device.etcher.LecLaserSocketDevice;
import com.honda.galc.client.device.etcher.LecLaserState;
import com.honda.galc.client.device.etcher.LecLaserStateMachine;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.DeviceMessageSeverity;

/**
 * @author Subu Kathiresan
 * @date May 26, 2017
 */
public abstract class BaseLecLaserStateAction implements ILecLaserStateAction {
	
	private LecLaserStateMachine stateMachine = null;
	private LecLaserState state = null;
	
	public abstract void processMessage(LecLaserResponse response);

	public void init() {}
	
	public void complete() {
		getStateMachine().transition();
	}
	
	public BaseLecLaserStateAction(LecLaserState state) {
		this.state = state;
	}
	
	public void sendMessage(LecLaserCommand cmd) {
		try {
			getDevice().send(cmd.getCommand());
		} catch(Exception ex) {
			ex.printStackTrace();
			String errMsg = "Unable to send message " + cmd.getCommand() + " to device " + getDevice().getId();
			notifyError(errMsg);
		}
	}
	
	public void notifyError(final String errMessage) {
		getLogger().error(errMessage);
		synchronized (getDevice().getListeners()) {
			for(final ILecLaserDeviceListener listener : getDevice().getListeners()) {
				Runnable notifyWorker = new Runnable() {
					public void run() {
						try {
							listener.handleDeviceStatusChange(errMessage, DeviceMessageSeverity.error);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				};

				Thread worker = new Thread(notifyWorker);
				worker.start();
			}
		}
	}
	
	public String[] getCommands() {
		return getStateMachine().getCommands();
	}
	
	public LecLaserStateMachine getStateMachine() {
		return stateMachine;
	}

	public void setStateMachine(LecLaserStateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}
	
	public LecLaserSocketDevice getDevice() {
		return getStateMachine().getDevice();
	}
	
	public Logger getLogger() {
		return getDevice().getLogger();
	}
	public LecLaserState getState() {
		return state;
	}

	public void setState(LecLaserState state) {
		this.state = state;
	}
}
