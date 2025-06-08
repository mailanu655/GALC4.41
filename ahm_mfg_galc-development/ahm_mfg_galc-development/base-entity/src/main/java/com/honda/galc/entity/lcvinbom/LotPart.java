package com.honda.galc.entity.lcvinbom;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.VinBomActiveStatus;

/**
 * The persistent class for the LOT_PART database table.
 * 
 */
@Entity
@Table(name="LOT_PART", schema="LCVINBOM")
public class LotPart extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private LotPartId id;

	@Column(name="ACTIVE", nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private VinBomActiveStatus active;

	public LotPart() {
	}

	public LotPartId getId() {
		return this.id;
	}

	public void setId(LotPartId id) {
		this.id = id;
	}

	public VinBomActiveStatus getActive() {
		return this.active;
	}

	public void setActive(VinBomActiveStatus active) {
		this.active = active;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		LotPart other = (LotPart) obj;
		if (active != other.active)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return toString(getId().getProductionLot(), getId().getLetSystemName(), 
				getId().getDcPartNumber(), getActive());
	}
	
}