/**
 * 
 */
package com.honda.galc.client.events;

/**
 * @author Subu Kathiresan
 * Oct 18, 2011
 */
public abstract class FloorStampRequest extends AbstractPlcDataReadyEvent {

	private String _lastVin = "";
	public abstract int getSendStatus();
	
	public FloorStampRequest(String applicationId, String plcDeviceId) {
		super(applicationId, plcDeviceId);
	}
	
	public String getLastVin() {
		return _lastVin;
	}
	
	public void setLastVin(String lastVin) {
		_lastVin = lastVin;
	}
}
