package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;


/**
 * The persistent class for the MC_MDRS_PERIOD_TBX database table.
 * 
 */
@Entity
@Table(name="MC_MDRS_PERIOD_TBX")
public class MCMdrsPeriod extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MCMdrsPeriodId id;

	public MCMdrsPeriod() {
	}

	public MCMdrsPeriodId getId() {
		return this.id;
	}

	public void setId(MCMdrsPeriodId id) {
		this.id = id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		MCMdrsPeriod other = (MCMdrsPeriod) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return toString( getId().getDeptCode(), getId().getLineNo(), getId().getProcessLocation()
				, getId().getPlantCode(), getId().getPlantLocCode()
				, getId().getShift(), getId().getShiftId()
				,getId().getQuarter(), getId().getPeriod());
	}

}