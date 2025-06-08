package com.honda.galc.visualoverview.shared;

public class View {
	
	private ViewId id;

    public ViewId getId() {
        return this.id;
    }

    public void setId(ViewId id) {
        this.id = id;
    }
	
	public String getViewId() {
		return getId().getViewId();
	}
	
	public String getLayerId() {
		return getId().getLayerId();
	}

}
