package com.honda.galc.device.dataformat;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class ProductIdIdentifier extends ProductId implements Serializable{
	private static final long serialVersionUID = 1L;
	private String productId;
	private String identifier;
	
	public ProductIdIdentifier() {
		super();
	}

	public ProductIdIdentifier(String productId, String identifier) {
		super(productId);
		this.identifier = StringUtils.trim(identifier);
	}

	public String getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = StringUtils.trim(identifier);
	}

	@Override
	public String toString() {
		return super.toString()+","+this.identifier;
	}
	
	
}
