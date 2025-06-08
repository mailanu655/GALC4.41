package com.honda.galc.client.events;

/**
 * @author Gangadhararao Gadde
 * @date Dec 04, 2012
 */
public class AfRfidWriteRequest extends AbstractPlcDataReadyEvent{
	
	private String vin = "";

	public AfRfidWriteRequest(String applicationId, String plcDeviceId) {
		super(applicationId, plcDeviceId);
	}
	
	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}
}
