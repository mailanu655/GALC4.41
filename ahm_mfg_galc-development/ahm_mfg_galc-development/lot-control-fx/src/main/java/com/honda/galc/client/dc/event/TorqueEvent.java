package com.honda.galc.client.dc.event;

import com.honda.galc.client.ui.IEvent;
import com.honda.galc.device.dataformat.Torque;
import com.honda.galc.openprotocol.model.LastTighteningResult;

/**
 * @author Subu Kathiresan
 * @date Dec 5, 2014
 */
public class TorqueEvent implements IEvent {
	
	private LastTighteningResult lastTighteningResult = null;
	
	public TorqueEvent(LastTighteningResult tighteningResult) {
		this.setLastTighteningResult(tighteningResult);
	}

	public LastTighteningResult getLastTighteningResult() {
		return lastTighteningResult;
	}

	public void setLastTighteningResult(LastTighteningResult lastTighteningResult) {
		this.lastTighteningResult = lastTighteningResult;
	}

	public Torque getTorque() {
		Torque torque = new Torque();
		
		torque.setAngle(lastTighteningResult.getAngle());
		torque.setAngleStatus(lastTighteningResult.getAngleStatus());
		
		torque.setTorqueValue(lastTighteningResult.getTorque());
		torque.setTorqueStatus(lastTighteningResult.getTorqueStatus());
		
		torque.setTighteningStatus(lastTighteningResult.getTighteningStatus());
		return torque;
	}
}
