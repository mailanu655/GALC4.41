/**
 * 
 */
package com.honda.galc.client.events;

/**
 * @author Subu Kathiresan
 * @date Feb 27, 2013
 */
public class ProductInspectionComplete {
	
	private String _productId = "";
	
	public ProductInspectionComplete(String productId) {
		_productId = productId;
	}
	
	public String getProductId() {
		return _productId;
	}
	
	public void setProductId(String productId) {
		_productId = productId;
	}
}
