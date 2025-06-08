
package com.honda.galc.dto.rest;

/**
 *
 * @author Wade Pei <br>
 * @date   Oct 24, 2013
 */
public class WeldProductTrackDTO {

	private String productId;
	private String productSpecCode;
	private String processPointId;
	private String orderNo;
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductSpecCode() {
		return productSpecCode;
	}
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
	public String getProcessPointId() {
		return processPointId;
	}
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
}
