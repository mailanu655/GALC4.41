/**
 * 
 */
package com.honda.galc.client.events;

/**
 * @author Subu Kathiresan
 * @date Dec 10, 2012
 */
public class FloorStampNextVinRequest extends FloorStampRequest {

	public FloorStampNextVinRequest(String applicationId, String plcDeviceId) {
		super(applicationId, plcDeviceId);
	}
	
	public int getSendStatus() {
		return 0;
	}
}
