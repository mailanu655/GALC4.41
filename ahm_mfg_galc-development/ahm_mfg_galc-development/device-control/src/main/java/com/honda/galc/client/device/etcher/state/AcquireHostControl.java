package com.honda.galc.client.device.etcher.state;

import com.honda.galc.client.device.etcher.LecLaserCommand;
import com.honda.galc.client.device.etcher.LecLaserResponse;
import com.honda.galc.client.device.etcher.LecLaserState;

/**
 * @author Subu Kathiresan
 * @date May 25, 2017
 */
public class AcquireHostControl extends BaseLecLaserStateAction {
	
	private static final int MAX_UNSUCCESSFUL_ATTEMPTS = 5;
	private int unsuccessfulAttempts = 0;
	
	public AcquireHostControl() {
		super(LecLaserState.acquireHostControl);
	}
	
	public void init() {
		sendMessage(LecLaserCommand.TakeHostControl);
	}

	public void processMessage(LecLaserResponse response) {
		if (response == LecLaserResponse.Success) {
			complete();
		} else {
			if (unsuccessfulAttempts >= MAX_UNSUCCESSFUL_ATTEMPTS ) {
				notifyError("Unable to take host control after " + MAX_UNSUCCESSFUL_ATTEMPTS + " attempts");
			}
			attemptToTakeHostControl();
		}
	}

	public void complete() {
		unsuccessfulAttempts = 0;
		getStateMachine().transition();
	}
	
	private void attemptToTakeHostControl() {
		try {
			Thread.sleep(500);
			unsuccessfulAttempts++;
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		sendMessage(LecLaserCommand.TakeHostControl);
	}
}
