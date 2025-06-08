/**
 * 
 */
package com.honda.galc.client.device.lotcontrol;

/**
 * @author Subu Kathiresan
 * @date Feb 28, 2014
 */
public class LaserEtchRequestItem {
	
	private String sendMessage = "";
	private String receivedMessage = "";
	

	public LaserEtchRequestItem(String sendMessage) {
		this.sendMessage = sendMessage;
	}
	
	public String getSendMessage() {
		return sendMessage;
	}
	
	public void setSendMessage(String sendMessage) {
		this.sendMessage = sendMessage;
	}

	public String getReceivedMessage() {
		return receivedMessage;
	}

	public void setReceivedMessage(String receivedMessage) {
		this.receivedMessage = receivedMessage;
	}
}