package com.honda.galc.oif.dto;

import com.honda.galc.util.OutputData;

public class PartShipmentProductDTO implements IOutputFormat {
	
	@OutputData(value="RECEIVING_SITE")
	private String receivingSite;
	@OutputData(value="PRODUCT_ID")
	private String productId;
	@OutputData(value="PRODUCT_SPEC_CODE")
	private String productSpecCode;
	@OutputData(value="ORDER_NUMBER")
	private String orderNumber;
	@OutputData(value="BUILD_STATUS")
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
	
	
}
