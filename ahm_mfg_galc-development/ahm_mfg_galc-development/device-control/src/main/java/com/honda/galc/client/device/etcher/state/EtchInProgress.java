package com.honda.galc.client.device.etcher.state;

import com.honda.galc.client.device.etcher.LecLaserCommand;
import com.honda.galc.client.device.etcher.LecLaserResponse;
import com.honda.galc.client.device.etcher.LecLaserState;

/**
 * @author Subu Kathiresan
 * @date May 25, 2017
 */
public class EtchInProgress extends BaseLecLaserStateAction {

	public EtchInProgress() {
		super(LecLaserState.etchInProgress);
	}

	public void init() {
		sendMessage(LecLaserCommand.GetJobExecutionStatus);
	}

	public void processMessage(LecLaserResponse response) {
		switch(response) {
		case Idle:
			complete();
			sendMessage(LecLaserCommand.ReleaseHostControl);
			break;
		case Busy:
			try {
				Thread.sleep(500);
			} catch(Exception ex){}
			
			sendMessage(LecLaserCommand.GetJobExecutionStatus);
			break;
		default:
			getLogger().warn("Unexpected response received: " + response.getResponseCode());
			notifyError("Laser device error: " + response.toString() + " - " + response.getMessage());
			break;
		}
	}
}
