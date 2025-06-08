package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class SubProductShippingDetailId implements Serializable {
	@Column(name="KD_LOT_NUMBER")
	private String kdLotNumber;

	@Column(name="PRODUCTION_LOT")
	private String productionLot;

	@Column(name="SUB_ID")
	private String subId;
	
	@Column(name="PRODUCT_SEQ_NO")
	private int productSeqNo;

	private static final long serialVersionUID = 1L;

	public SubProductShippingDetailId() {
		super();
	}

	public String getKdLotNumber() {
		return StringUtils.trim(this.kdLotNumber);
	}

	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}
	
	public String getProductionLot() {
		return StringUtils.trim(this.productionLot);
	}

	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}


	public String getSubId() {
		return StringUtils.trim(this.subId);
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}

	public int getProductSeqNo() {
		return this.productSeqNo;
	}

	public void setProductSeqNo(int productSeqNo) {
		this.productSeqNo = productSeqNo;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof SubProductShippingDetailId)) {
			return false;
		}
		SubProductShippingDetailId other = (SubProductShippingDetailId) o;
		return this.getKdLotNumber().equals(getKdLotNumber())
		    && this.getProductionLot().equals(other.getProductionLot())
			&& this.getSubId().equals(other.getSubId())
			&& (this.getProductSeqNo() == other.getProductSeqNo());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.getKdLotNumber().hashCode();
		hash = hash * prime + this.getSubId().hashCode();
		hash = hash * prime + ((int) this.getProductSeqNo());
		return hash;
	}

}
