
package com.honda.galc.dto.rest;

/**
 *
 * @author Wade Pei <br>
 * @date   Oct 24, 2013
 */
public class ProductTrackDTO {

	private String productId;
	private String productSpecCode;
	private String processPointId;
	private String orderNo;
	private String INFO_CODE;
	private String INFO_MSG;
	
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
	
	public String getINFO_CODE() {
		return INFO_CODE;
	}
	public void setINFO_CODE(String iNFO_CODE) {
		INFO_CODE = iNFO_CODE;
	}
	public String getINFO_MSG() {
		return INFO_MSG;
	}
	public void setINFO_MSG(String iNFO_MSG) {
		INFO_MSG = iNFO_MSG;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	} 
}
