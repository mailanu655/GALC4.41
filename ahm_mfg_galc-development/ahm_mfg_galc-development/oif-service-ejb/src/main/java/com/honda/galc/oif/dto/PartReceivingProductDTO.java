package com.honda.galc.oif.dto;

import com.honda.galc.util.GPCSData;


public class PartReceivingProductDTO implements IOutputFormat {
	
	@GPCSData("RECEIVING_SITE")
	private String receivingSite;
	@GPCSData("PRODUCT_ID")
	private String productId;
	@GPCSData("PRODUCT_SPEC_CODE")
	private String productSpecCode;
	@GPCSData("ORDER_NUMBER")
	private String orderNumber;
	@GPCSData("BUILD_STATUS")
	private String buildStatus;
	
	
	public String getReceivingSite() {
		return receivingSite;
	}
	public String getProductId() {
		return productId;
	}
	public String getProductSpecCode() {
		return productSpecCode;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public String getBuildStatus() {
		return buildStatus;
	}
	public void setReceivingSite(String receivingSite) {
		this.receivingSite = receivingSite;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public void setBuildStatus(String buildStatus) {
		this.buildStatus = buildStatus;
	}
	
	@Override
	public String toString() {
		return "PartReceivingProductDTO [receivingSite=" + receivingSite + ", productId=" + productId
				+ ", productSpecCode=" + productSpecCode + ", orderNumber=" + orderNumber + ", buildStatus="
				+ buildStatus + "]";
	}
	
	
}
