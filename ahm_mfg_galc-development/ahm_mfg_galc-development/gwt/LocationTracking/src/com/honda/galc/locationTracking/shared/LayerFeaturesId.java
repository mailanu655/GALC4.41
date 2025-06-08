package com.honda.galc.locationTracking.shared;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public class LayerFeaturesId {

    private String layerId;
    private String featureId;

    public String getLayerId() {
        return layerId;
    }

    public void setLayerId(String layerId) {
        this.layerId = layerId;
    }

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }
}
