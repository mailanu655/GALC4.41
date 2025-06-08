/**
 * 
 */
package com.honda.galc.client.events;

/**
 * @author Subu Kathiresan
 * @date Feb 20, 2013
 */
public class ProductInspectionRequest {

	private String _productId = "";
	private String _carrierNumber = "";
	
	public ProductInspectionRequest(String productId) {
		_productId = productId;
	}
	
	public ProductInspectionRequest(String productId, String carrierNumber) {
		_productId = productId;
		_carrierNumber = carrierNumber;
	}
	
	public String getProductId() {
		return _productId;
	}
	
	public void setProductId(String productId) {
		_productId = productId;
	}
	
	public String getCarrierNumber() {
		return _carrierNumber;
	}
	
	public void setCarrierNumber(String carrierNumber) {
		_carrierNumber = carrierNumber;
	}
}
