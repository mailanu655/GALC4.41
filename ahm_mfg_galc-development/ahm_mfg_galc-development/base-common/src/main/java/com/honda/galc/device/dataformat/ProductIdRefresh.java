package com.honda.galc.device.dataformat;

import java.io.Serializable;

public class ProductIdRefresh extends InputData implements Serializable{
	private static final long serialVersionUID = 1L;
	private boolean productIdRefresh;
	
	public ProductIdRefresh() {
		super();
	}

	public ProductIdRefresh(boolean isRefresh) {
		super();
		this.productIdRefresh = isRefresh;
	}

	public boolean isProductIdRefresh() {
		return productIdRefresh;
	}

	public void setProductIdRefresh(boolean productIdRefresh) {
		this.productIdRefresh = productIdRefresh;
	}
    
}
