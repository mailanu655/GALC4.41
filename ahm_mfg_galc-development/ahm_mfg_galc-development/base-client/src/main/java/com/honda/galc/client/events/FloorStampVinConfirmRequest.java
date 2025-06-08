/**
 * 
 */
package com.honda.galc.client.events;


/**
 * @author Subu Kathiresan
 * @date Nov 19, 2012
 */
public class FloorStampVinConfirmRequest extends FloorStampRequest {

	public FloorStampVinConfirmRequest(String applicationId, String plcDeviceId) {
		super(applicationId, plcDeviceId);
	}
	
	public int getSendStatus() {
		return 1;
	}
}
