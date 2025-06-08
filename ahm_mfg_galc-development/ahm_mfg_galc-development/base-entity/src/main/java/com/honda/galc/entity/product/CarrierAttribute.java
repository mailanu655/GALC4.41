package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name="CARRIER_ATTRIBUTES_TBX")
public class CarrierAttribute extends AuditEntry {

	@EmbeddedId
	private CarrierAttributeId id;

	@Column(name="ATTRIBUTE_VALUE")
	private String attributeValue;

	private static final long serialVersionUID = 1L;

	public CarrierAttribute() {
		super();
	}

	public CarrierAttribute(String trackingArea, String carrierNumber, String attribute, String attributeValue) {
		this.id = new CarrierAttributeId(trackingArea, carrierNumber, attribute);
		this.attributeValue = attributeValue;
	}

	public CarrierAttributeId getId() {
		return id;
	}

	public  void setId(CarrierAttributeId id) {
		this.id = id;
	}

	public String getAttributeValue() {
		return StringUtils.trim(this.attributeValue);
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.attributeValue == null) ? 0 : this.attributeValue.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CarrierAttribute other = (CarrierAttribute) obj;
		if (attributeValue == null) {
			if (other.attributeValue != null) {
				return false;
			}
		} else if (!attributeValue.equals(other.attributeValue)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "CarrierAttribute [id=" + this.id + ", attributeValue=" + this.attributeValue + "]";
	}

}
