package com.honda.galc.device.dataformat;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.device.IDeviceData;

public class ProductIdHighlight implements IDeviceData, Serializable {

	private static final long serialVersionUID = 1L;

	private String productId;
	private String identifier;

	public ProductIdHighlight() {
		super();
	}

	public ProductIdHighlight(String productId, String identifier) {
		super();
		this.productId = productId;
		this.identifier = identifier;
	}

	public String getProductId() {
		return StringUtils.trimToEmpty(this.productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getIdentifier() {
		return StringUtils.trimToEmpty(this.identifier);
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public String toString() {
		return getIdentifier() + "," + getProductId();
	}
}
