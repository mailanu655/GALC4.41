/**
 * 
 */
package com.honda.galc.client.events;


/**
 * @author Gangadhararao Gadde
 * @date Nov 29, 2012
 */
public class PaintRfidWriteRequest extends AbstractPlcDataReadyEvent {

	private String model = "";
	private String sequenceNumber = "";
	private String vin="";
	
	public PaintRfidWriteRequest(String applicationId, String plcDeviceId) {
		super(applicationId, plcDeviceId);
	}
	
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}
}
