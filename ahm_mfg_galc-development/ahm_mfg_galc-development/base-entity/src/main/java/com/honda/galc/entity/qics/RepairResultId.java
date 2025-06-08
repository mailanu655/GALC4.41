package com.honda.galc.entity.qics;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class RepairResultId implements Serializable {
	@Column(name="REPAIR_ID")
	private int repairId;

	@Column(name="PRODUCT_ID")
	private String productId;

	@Column(name="DEFECTRESULTID")
	private int defectresultid;

	private static final long serialVersionUID = 1L;

	public RepairResultId() {
		super();
	}

	public int getRepairId() {
		return this.repairId;
	}

	public void setRepairId(int repairId) {
		this.repairId = repairId;
	}

	public String getProductId() {
		return StringUtils.trim(this.productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getDefectresultid() {
		return this.defectresultid;
	}

	public void setDefectresultid(int defectresultid) {
		this.defectresultid = defectresultid;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof RepairResultId)) {
			return false;
		}
		RepairResultId other = (RepairResultId) o;
		return (this.repairId == other.repairId)
			&& this.productId.equals(other.productId)
			&& (this.defectresultid == other.defectresultid);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.repairId;
		hash = hash * prime + this.productId.hashCode();
		hash = hash * prime + this.defectresultid;
		return hash;
	}

}
