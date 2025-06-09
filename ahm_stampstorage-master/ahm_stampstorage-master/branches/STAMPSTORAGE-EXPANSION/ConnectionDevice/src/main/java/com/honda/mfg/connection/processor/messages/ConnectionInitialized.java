package com.honda.mfg.connection.processor.messages;

import java.util.Date;

/**
 * User: vcc30690 Date: 4/21/11
 */
public class ConnectionInitialized {
	private Date initializedTimestamp;

	public ConnectionInitialized() {
		super();
		this.initializedTimestamp = new Date();
	}

	public Date getInitializedTime() {
		return initializedTimestamp;
	}
}
