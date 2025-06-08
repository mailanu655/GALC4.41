package com.honda.galc.entity.gts;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class GtsProductId implements Serializable {
	@Column(name="TRACKING_AREA")
	private String trackingArea;

	@Column(name="PRODUCT_ID")
	private String productId;

	private static final long serialVersionUID = 1L;

	public GtsProductId() {
		super();
	}
	
	public GtsProductId(String areaName, String productId) {
		this.trackingArea = areaName;
		this.productId = productId;
	}

	public String getTrackingArea() {
		return StringUtils.trimToEmpty(this.trackingArea);
	}

	public void setTrackingArea(String trackingArea) {
		this.trackingArea = trackingArea;
	}

	public String getProductId() {
		return StringUtils.trimToEmpty(this.productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof GtsProductId)) {
			return false;
		}
		GtsProductId other = (GtsProductId) o;
		return this.getTrackingArea().equals(other.getTrackingArea())
			&& this.getProductId().equals(other.getProductId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.getTrackingArea().hashCode();
		hash = hash * prime + this.getProductId().hashCode();
		return hash;
	}

}
