package com.honda.mfg.connection.processor.messages;

/**
 * @author VCC44349 Message to indicate that networking errors were encountered
 *
 */
public class ConnectionExTrigger {

	private int exCount = 0;

	public int getExCount() {
		return exCount;
	}

	public void setExCount(int exCount) {
		this.exCount = exCount;
	}

	public ConnectionExTrigger() {
		super();
	}
}
