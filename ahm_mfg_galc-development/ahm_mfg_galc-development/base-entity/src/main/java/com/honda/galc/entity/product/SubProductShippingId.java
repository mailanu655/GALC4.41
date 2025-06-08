package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class SubProductShippingId implements Serializable {
	@Column(name="KD_LOT_NUMBER")
	private String kdLotNumber;

	@Column(name="PRODUCTION_LOT")
	private String productionLot;


	private static final long serialVersionUID = 1L;

	public SubProductShippingId() {
		super();
	}
	
	public SubProductShippingId(String kdLotNumber,String productionLot) {
		super();
		this.kdLotNumber = kdLotNumber;
		this.productionLot = productionLot;
	}

	public String getKdLotNumber() {
		return StringUtils.trim(this.kdLotNumber);
	}

	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}
	
	public String getProductionLot() {
		return StringUtils.trim( productionLot);
	}

	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}


	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof SubProductShippingId)) {
			return false;
		}
		SubProductShippingId other = (SubProductShippingId) o;
		return this.getKdLotNumber().equals(getKdLotNumber())
			&& this.getProductionLot().equals(other.getProductionLot());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.getKdLotNumber().hashCode();
		hash = hash * prime + this.getProductionLot().hashCode();
		return hash;
	}


}
