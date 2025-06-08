package com.honda.galc.client.ui.event;

import com.honda.galc.client.product.mvc.PaneId;

public class ProductInvalidEvent extends ProductResultEvent {
	private String message;
	
	public ProductInvalidEvent(PaneId paneId) {
		super(paneId);
	}
	public ProductInvalidEvent(String message) {
		super();
		this.message = message;
	}
	
	public ProductInvalidEvent(PaneId paneId, String message) {
		super(paneId);
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
