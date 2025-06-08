/**
 * 
 */
package com.honda.galc.client.events;

/**
 * @author Gangadhararao Gadde
 * @date Dec 21, 2012
 */
public class PlasticsIPCarrierUnloadRequest extends AbstractPlcDataReadyEvent {

	private Integer _carrierNumber = -1;

	public PlasticsIPCarrierUnloadRequest(String applicationId, String plcDeviceId) {
		super(applicationId, plcDeviceId);
	}
	
	public Integer getCarrierNumber() {
		return _carrierNumber;
	}

	public void setCarrierNumber(Integer carrierNumber) {
		_carrierNumber = carrierNumber;
	}
	
}
