package com.honda.galc.client.teamleader;

import com.honda.galc.data.ProductType;

public class ShipScrapEvent {
	private ProductType productType;
	private boolean isShipped;
	
	public ShipScrapEvent(ProductType productType, boolean isShipped) {
		super();
		this.productType = productType;
		this.isShipped = isShipped;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public boolean isShipped() {
		return isShipped;
	}

	public void setShipped(boolean isShipped) {
		this.isShipped = isShipped;
	}
	
}
