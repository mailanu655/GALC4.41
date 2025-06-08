package com.honda.galc.device.dataformat;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class ProductId extends InputData implements Serializable{
	private static final long serialVersionUID = 1L;
	protected String productId;
	
	public ProductId() {
		super();
	}

	public ProductId(String productId) {
		super();
		this.productId = StringUtils.trim(productId);
	}

	// Getters & Setters
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = StringUtils.trim(productId);
	}

	public void setProductIdWithoutTrim(String productId) {
		this.productId = productId;
	}

	@Override
	public String toString() {
		return this.productId;
	}
	
	
}
