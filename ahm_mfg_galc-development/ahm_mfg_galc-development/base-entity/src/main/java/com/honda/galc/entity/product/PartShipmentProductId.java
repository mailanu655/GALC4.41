package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class PartShipmentProductId implements Serializable{

	@Column(name = "SHIPMENT_ID")
	private Integer shipmentId;

	@Column(name = "PRODUCT_ID")
    private String productId;

	private static final long serialVersionUID = 1L;

	public Integer getShipmentId() {
		return shipmentId;
	}

	public String getProductId() {
		return StringUtils.trim(productId);
	}

	public void setShipmentId(Integer shipmentId) {
		this.shipmentId = shipmentId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((shipmentId == null) ? 0 : shipmentId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PartShipmentProductId other = (PartShipmentProductId) obj;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (shipmentId == null) {
			if (other.shipmentId != null)
				return false;
		} else if (!shipmentId.equals(other.shipmentId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PartShipmentProductId [shipmentId=" + shipmentId + ", productId=" + productId + "]";
	}
	  
	  
	  
}
