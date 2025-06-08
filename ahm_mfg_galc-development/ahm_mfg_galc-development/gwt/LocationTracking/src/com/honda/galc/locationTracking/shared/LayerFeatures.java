package com.honda.galc.locationTracking.shared;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public class LayerFeatures {
	
	private LayerFeaturesId id;

    public LayerFeaturesId getId() {
        return this.id;
    }

    public void setId(LayerFeaturesId id) {
        this.id = id;
    }
	
	public String getFeatureId() {
		return getId().getFeatureId();
	}
	
	public String getLayerId() {
		return getId().getLayerId();
	}

}
