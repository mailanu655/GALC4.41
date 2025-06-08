package com.honda.galc.client.ui.event;

import com.honda.galc.client.product.mvc.PaneId;

public class ProcessCompleteEvent extends ProductResultEvent {
	
	public ProcessCompleteEvent() {
		super();
	}
	
	public ProcessCompleteEvent(PaneId paneId) {
		super(paneId);
	}
}
