/**
 * 
 */
package com.honda.galc.client.events;

/**
 * @author Subu Kathiresan
 * @date Sep 4, 2013
 */
public class PinStampRequest extends AbstractPlcDataReadyEvent {

	private String infoCode = "";
	private String infoMessage = "";
	private String vin = "";

	public PinStampRequest(String applicationId, String plcDeviceId) {
		super(applicationId, plcDeviceId);
	}

	public String getInfoCode() {
		return infoCode;
	}

	public void setInfoCode(String infoCode) {
		this.infoCode = infoCode;
	}

	public String getInfoMessage() {
		return infoMessage;
	}

	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}
	
	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}
}
