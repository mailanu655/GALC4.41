package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class ViewId implements Serializable {
    @Column(name = "VIEW_ID")
    private String viewId;

    @Column(name = "LAYER_ID")
    private String layerId;

    private static final long serialVersionUID = 1L;

    public ViewId() {
        super();
    }

    public ViewId(String viewId, String layerId) {
        this.viewId = viewId;
    	this.layerId = layerId;
    }

    public String getViewId() {
        return StringUtils.trim(viewId);
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public String getLayerId() {
        return StringUtils.trim(layerId);
    }

    public void setLayerId(String layerId) {
        this.layerId = layerId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ViewId)) {
            return false;
        }
        ViewId other = (ViewId) o;
        return this.viewId.equals(other.viewId)
                && this.layerId.equals(other.layerId);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.viewId.hashCode();
        hash = hash * prime + this.layerId.hashCode();
        return hash;
    }

}
