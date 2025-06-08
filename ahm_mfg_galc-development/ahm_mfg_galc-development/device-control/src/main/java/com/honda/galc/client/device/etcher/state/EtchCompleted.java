package com.honda.galc.client.device.etcher.state;

import com.honda.galc.client.device.etcher.ILecLaserDeviceListener;
import com.honda.galc.client.device.etcher.LecLaserResponse;
import com.honda.galc.client.device.etcher.LecLaserState;

/**
 * @author Subu Kathiresan
 * @date May 25, 2017
 */
public class EtchCompleted extends BaseLecLaserStateAction {

	public EtchCompleted() {
		super(LecLaserState.etchCompleted);
	}

	public void processMessage(LecLaserResponse response) {
		if (response == LecLaserResponse.Success) {
			complete();
		} else {
			getLogger().warn("Could not release host control");
		}
	}
	
	public void complete() {
		getStateMachine().transition();
		notifyEtchComplete();
	}
	
	public void notifyEtchComplete() {
		synchronized (getDevice().getListeners()) {
			for(final ILecLaserDeviceListener listener : getDevice().getListeners()) {
				Runnable notifyWorker = new Runnable() {
					public void run() {
						try {
							listener.etchCompleted(getDevice().getId(), getDevice().getCurrentProductId());
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
}
