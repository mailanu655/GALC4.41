package com.honda.galc.client.teamleader.model;

public class ProductUpdateStatus {
	private String product;
	private String productSpecCode;
	private boolean isCreated;
	
	public ProductUpdateStatus(String product, boolean isCreated, String productSpecCode) {
		super();
		this.product = product;
		this.isCreated = isCreated;
		this.productSpecCode = productSpecCode;
	}

	public String getProduct() {
		return product;
	}

	public boolean isCreated() {
		return isCreated;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public void setCreated(boolean isCreated) {
		this.isCreated = isCreated;
	}

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	
}
