package com.honda.galc.entity.lcvinbom;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;


/**
 * The persistent class for the VIN_PART database table.
 * 
 */
@Entity
@Table(name="VIN_PART", schema="LCVINBOM")
public class VinPart extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private VinPartId id;

	@Column(name="SHIP_STATUS", nullable=false, columnDefinition="INT(1)")
	private boolean shipStatus;

	public VinPart() {
	}

	public VinPartId getId() {
		return this.id;
	}

	public void setId(VinPartId id) {
		this.id = id;
	}

	public boolean getShipStatus() {
		return this.shipStatus;
	}

	public void setShipStatus(boolean shipStatus) {
		this.shipStatus = shipStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (shipStatus ? 1231 : 1237);
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
		VinPart other = (VinPart) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (shipStatus != other.shipStatus)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return toString(getId().getProductId(), getId().getLetSystemName(), 
				getId().getDcPartNumber(), getShipStatus());
	}
	
}