package com.honda.galc.client.dc.event;

import com.honda.galc.client.ui.IEvent;
import com.honda.galc.openprotocol.model.CommandAccepted;

public class PsetCommandAcceptedEvent implements IEvent {
	
	String deviceId;
	
	CommandAccepted commandAccepted;

	public PsetCommandAcceptedEvent(String deviceId, CommandAccepted commandAccepted) {
		this.deviceId = deviceId;
		this.commandAccepted = commandAccepted;
	}

}