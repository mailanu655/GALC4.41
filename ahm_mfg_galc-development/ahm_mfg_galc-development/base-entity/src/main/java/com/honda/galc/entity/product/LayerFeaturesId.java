package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class LayerFeaturesId implements Serializable {
    @Column(name = "LAYER_ID")
    private String layerId;

    @Column(name = "FEATURE_ID")
    private String featureId;

    private static final long serialVersionUID = 1L;

    public LayerFeaturesId() {
        super();
    }

    public LayerFeaturesId(String layerId, String featureId) {
        this.layerId = layerId;
    	this.featureId = featureId;
    }

    public String getLayerId() {
        return StringUtils.trim(layerId);
    }

    public void setLayerId(String layerId) {
        this.layerId = layerId;
    }

    public String getFeatureId() {
        return StringUtils.trim(featureId);
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LayerFeaturesId)) {
            return false;
        }
        LayerFeaturesId other = (LayerFeaturesId) o;
        return this.layerId.equals(other.layerId)
                && this.featureId.equals(other.featureId);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.layerId.hashCode();
        hash = hash * prime + this.featureId.hashCode();
        return hash;
    }

}
