package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name="SCHEDULE_REP_TBX")
public class ScheduleReplication extends AuditEntry{
	
	@EmbeddedId
	private ScheduleReplicationId id;

	@Column( name = "SUB_ASSY_PROD_ID_FORMAT" )
	private String subAssyProdIdFormat;

	@Column( name = "PROD_DATE_OFFSET" )
	private int prodDateOffset;

	@Override
	public String toString() {
		return "ScheduleReplication [id=" + id + ", subAssyProdIdFormat=" + subAssyProdIdFormat + ", prodDateOffset="
				+ prodDateOffset + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + prodDateOffset;
		result = prime * result + ((subAssyProdIdFormat == null) ? 0 : subAssyProdIdFormat.hashCode());
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
		ScheduleReplication other = (ScheduleReplication) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (prodDateOffset != other.prodDateOffset)
			return false;
		if (subAssyProdIdFormat == null) {
			if (other.subAssyProdIdFormat != null)
				return false;
		} else if (!subAssyProdIdFormat.equals(other.subAssyProdIdFormat))
			return false;
		return true;
	}

	@Override
	public ScheduleReplicationId getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public String getSubAssyProdIdFormat() {
		return subAssyProdIdFormat;
	}

	public void setSubAssyProdIdFormat(String subAssyProdIdFormat) {
		this.subAssyProdIdFormat = subAssyProdIdFormat;
	}

	public int getProdDateOffset() {
		return prodDateOffset;
	}

	public void setProdDateOffset(int prodDateOffset) {
		this.prodDateOffset = prodDateOffset;
	}

}
