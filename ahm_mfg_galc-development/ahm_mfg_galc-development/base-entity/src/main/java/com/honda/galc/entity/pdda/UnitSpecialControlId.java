package com.honda.galc.entity.pdda;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * Apr 09, 2014
 */
@Embeddable
public class UnitSpecialControlId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="MAINTENANCE_ID", nullable=false)
	private int maintenanceId;

	@Column(name="CONTROL_APPLIED", nullable=false, length=254)
	private String controlApplied;

    public UnitSpecialControlId() {}
    
	public int getMaintenanceId() {
		return this.maintenanceId;
	}
	
	public void setMaintenanceId(int maintenanceId) {
		this.maintenanceId = maintenanceId;
	}
	
	public String getControlApplied() {
		return StringUtils.trim(this.controlApplied);
	}
	
	public void setControlApplied(String controlApplied) {
		this.controlApplied = controlApplied;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((controlApplied == null) ? 0 : controlApplied.hashCode());
		result = prime * result + maintenanceId;
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
		UnitSpecialControlId other = (UnitSpecialControlId) obj;
		if (controlApplied == null) {
			if (other.controlApplied != null)
				return false;
		} else if (!controlApplied.equals(other.controlApplied))
			return false;
		if (maintenanceId != other.maintenanceId)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getMaintenanceId(), getControlApplied());
	}
}