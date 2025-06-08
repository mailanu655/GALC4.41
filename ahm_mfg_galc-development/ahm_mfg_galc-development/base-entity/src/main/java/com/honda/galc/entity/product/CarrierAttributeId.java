package com.honda.galc.entity.product;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class CarrierAttributeId implements Serializable {

	@Column(name="TRACKING_AREA")
	private String trackingArea;

	@Column(name="CARRIER_NUMBER")
	private String carrierNumber;

	@Column(name="ATTRIBUTE")
	private String attribute;

	private static final long serialVersionUID = 1L;

	public CarrierAttributeId() {
		super();
	}

	public CarrierAttributeId(String trackingArea, String carrierNumber, String attribute) {
		this.trackingArea = trackingArea;
		this.carrierNumber = carrierNumber;
		this.attribute = attribute;
	}

	public String getTrackingArea() {
		return StringUtils.trim(this.trackingArea);
	}

	public void setTrackingArea(String trackingArea) {
		this.trackingArea = trackingArea;
	}

	public String getCarrierNumber() {
		return StringUtils.trim(this.carrierNumber);
	}

	public void setCarrierNumber(String carrierNumber) {
		this.carrierNumber = carrierNumber;
	}

	public String getAttribute() {
		return StringUtils.trim(this.attribute);
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof CarrierAttributeId)) {
			return false;
		}
		CarrierAttributeId other = (CarrierAttributeId) o;
		return getTrackingArea().equals(other.getTrackingArea())
				&& getCarrierNumber().equals(other.getCarrierNumber())
				&& getAttribute().equals(other.getAttribute());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + getTrackingArea().hashCode();
		hash = hash * prime + getCarrierNumber().hashCode();
		hash = hash * prime + getAttribute().hashCode();
		return hash;
	}

	@Override
	public String toString() {
		return "CarrierAttributeId [trackingArea=" + this.trackingArea + ", carrierNumber=" + this.carrierNumber + ", attribute=" + this.attribute + "]";
	}

}
