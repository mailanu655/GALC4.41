package com.honda.galc.entity.lcvinbom;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;


/**
 * The persistent class for the MODEL_LOT database table.
 * 
 */
@Entity
@Table(name="MODEL_LOT", schema="LCVINBOM")
public class ModelLot extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ModelLotId id;

	@Column(name="STARTING_PRODUCTION_LOT", nullable=false, length=20)
	private String startingProductionLot;

	public ModelLot() {
	}

	public ModelLotId getId() {
		return this.id;
	}

	public void setId(ModelLotId id) {
		this.id = id;
	}

	public String getStartingProductionLot() {
		return StringUtils.trim(this.startingProductionLot);
	}

	public void setStartingProductionLot(String startingProductionLot) {
		this.startingProductionLot = startingProductionLot;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((startingProductionLot == null) ? 0 : startingProductionLot.hashCode());
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
		ModelLot other = (ModelLot) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (startingProductionLot == null) {
			if (other.startingProductionLot != null)
				return false;
		} else if (!startingProductionLot.equals(other.startingProductionLot))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return toString(getId().getModelPartId(), getId().getPlanCode(), 
				getStartingProductionLot());
	}
}