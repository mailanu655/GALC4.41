package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductStampingSequenceId implements Serializable{
	      
	@Column(name = "PRODUCTION_LOT",nullable=false, length = 20)
	private String productionLot;
	
	@Column(name = "PRODUCT_ID", nullable=false, length = 17)
	private String productID;

	 private static final long serialVersionUID = 1L;
	 
	public ProductStampingSequenceId(String productionLot, String productID) {
		super();
		this.productionLot = productionLot;
		this.productID = productID;
	}

	public ProductStampingSequenceId() {
		super();
	}

	public String getProductID() {
	    return productID;
	}

	public void setProductID(String productID) {
	    this.productID = productID;
	}

	public String getProductionLot() {
	    return productionLot;
	}

	public void setProductionLot(String productionLot) {
	    this.productionLot = productionLot;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((productID == null) ? 0 : productID.hashCode());
		result = prime * result
				+ ((productionLot == null) ? 0 : productionLot.hashCode());
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
		ProductStampingSequenceId other = (ProductStampingSequenceId) obj;
		if (productID == null) {
			if (other.productID != null)
				return false;
		} else if (!productID.equals(other.productID))
			return false;
		if (productionLot == null) {
			if (other.productionLot != null)
				return false;
		} else if (!productionLot.equals(other.productionLot))
			return false;
		return true;
	}

	
}