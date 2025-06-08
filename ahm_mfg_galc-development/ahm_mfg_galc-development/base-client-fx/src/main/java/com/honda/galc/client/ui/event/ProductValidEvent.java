package com.honda.galc.client.ui.event;

import com.honda.galc.client.product.mvc.PaneId;

public class ProductValidEvent extends ProductResultEvent {

	public ProductValidEvent() {
		super();
	}
	
	public ProductValidEvent(PaneId paneId) {
		super(paneId);
	}
}
