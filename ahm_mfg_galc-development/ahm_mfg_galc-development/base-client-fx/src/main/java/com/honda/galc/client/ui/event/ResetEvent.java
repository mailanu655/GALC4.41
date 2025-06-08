package com.honda.galc.client.ui.event;

import com.honda.galc.client.product.mvc.FXViewId;
import com.honda.galc.client.ui.IEvent;

public class ResetEvent implements IEvent {
	FXViewId fxViewId;
	
	public ResetEvent(FXViewId id) {
		this.fxViewId = id;
	}
	
	public FXViewId getFXViewId() {
		return this.fxViewId;
	}
	
	public void setFXViewId(FXViewId id) {
		this.fxViewId = id;
	}
}
