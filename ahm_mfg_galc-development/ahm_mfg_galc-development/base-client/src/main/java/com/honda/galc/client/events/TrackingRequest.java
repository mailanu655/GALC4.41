/**
 * 
 */
package com.honda.galc.client.events;


/**
 * @author Gangadhararao Gadde
 * @date Nov 29, 2012
 */
public class TrackingRequest extends AbstractPlcDataReadyEvent {

	private String productId = "";

	public TrackingRequest(String applicationId, String plcDeviceId) {
		super(applicationId, plcDeviceId);
	}
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
}
