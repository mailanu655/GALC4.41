package com.honda.galc.client.device.etcher.state;

import com.honda.galc.client.device.etcher.LecLaserResponse;
import com.honda.galc.client.device.etcher.LecLaserState;

/**
 * @author Subu Kathiresan
 * @date May 26, 2017
 */
public class SendCommands extends BaseLecLaserStateAction {

	private volatile int commandIndex = 0;
	
	public SendCommands() {
		super(LecLaserState.sendCommands);
	}
	
	public int getCommandIndex() {
		return commandIndex;
	}

	public void setCommandIndex(int commandIndex) {
		this.commandIndex = commandIndex;
	}

	public void init() {
		sendCommand();
	}

	public void processMessage(LecLaserResponse response) {
		if (response == LecLaserResponse.Success) {
			if (commandIndex < getCommands().length) {
				sendCommand();
			} else {
				complete();			
			}
		} else {
			getLogger().warn("Command " + getCommands()[getCommandIndex()] + " was not successful");
			notifyError("Laser device error: " + response.toString() + " - " + response.getMessage());
		}
	}

	public void complete() {
		commandIndex = 0;
		getStateMachine().transition();
	}
	
	private void sendCommand() {
		String cmd = getCommands()[commandIndex++];
		getLogger().info("Sending command[" + commandIndex + "]: " + cmd);
		getDevice().send(cmd);
	}
}
