package com.honda.galc.client.ui.event;

import com.honda.galc.client.product.mvc.PaneId;
import com.honda.galc.client.ui.IEvent;

public class ProductResultEvent implements IEvent {
	PaneId paneId;

	public ProductResultEvent() {
	}

	public ProductResultEvent(PaneId paneId) {
		this.paneId = paneId;
	}
	
	public PaneId getPaneId() {
		return this.paneId;
	}
	
	public void setPaneId(PaneId paneId) {
		this.paneId = paneId;
	}
}