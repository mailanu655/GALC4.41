/**
 * 
 */
package com.honda.galc.client.events;

/**
 * @author Subu Kathiresan
 * @date Nov 28, 2012
 */
public class PlcDataCollectionRequest extends AbstractPlcDataReadyEvent {

	private String _productId = "";
	
	public PlcDataCollectionRequest(String applicationId, String plcDeviceId) {
		super(applicationId, plcDeviceId);
	}
	
	public String getProductId() {
		return _productId;
	}
	
	public void setProductId(String productId) {
		_productId = productId;
	}
}
