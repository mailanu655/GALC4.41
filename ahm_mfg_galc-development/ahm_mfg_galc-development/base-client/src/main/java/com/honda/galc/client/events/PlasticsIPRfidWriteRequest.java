/**
 * 
 */
package com.honda.galc.client.events;


/**
 * @author Subu Kathiresan
 * @date Nov 5, 2012
 */
public class PlasticsIPRfidWriteRequest extends AbstractPlcDataReadyEvent {

	private Integer _carrierNumber = -1;
	private Integer _carrierCapacity = -1;
	private String _carrierType = "";
	
	public PlasticsIPRfidWriteRequest(String applicationId, String plcDeviceId) {
		super(applicationId, plcDeviceId);
	}
	
	public Integer getCarrierNumber() {
		return _carrierNumber;
	}
	
	public void setCarrierNumber(Integer carrierNumber) {
		_carrierNumber = carrierNumber;
	}
	
	public String getCarrierType() {
		return _carrierType;
	}

	public void setCarrierType(String carrierType) {
		_carrierType = carrierType;
	}
	
	public void setCarrierType(Integer carrierType) {
		_carrierType = String.valueOf(carrierType);
	}
	
	public Integer getCarrierCapacity() {
		return _carrierCapacity;
	}

	public void setCarrierCapacity(Integer carrierCapacity) {
		_carrierCapacity = carrierCapacity;
	}
}
