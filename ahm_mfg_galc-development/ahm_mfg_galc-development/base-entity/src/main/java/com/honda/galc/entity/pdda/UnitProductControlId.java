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
public class UnitProductControlId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="MAINTENANCE_ID", nullable=false)
	private int maintenanceId;

	@Column(name="CONTROL_STANDARD", nullable=false, length=250)
	private String controlStandard;

    public UnitProductControlId() {}
    
	public int getMaintenanceId() {
		return this.maintenanceId;
	}
	
	public void setMaintenanceId(int maintenanceId) {
		this.maintenanceId = maintenanceId;
	}
	
	public String getControlStandard() {
		return StringUtils.trim(this.controlStandard);
	}
	
	public void setControlStandard(String controlStandard) {
		this.controlStandard = controlStandard;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((controlStandard == null) ? 0 : controlStandard.hashCode());
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
		UnitProductControlId other = (UnitProductControlId) obj;
		if (controlStandard == null) {
			if (other.controlStandard != null)
				return false;
		} else if (!controlStandard.equals(other.controlStandard))
			return false;
		if (maintenanceId != other.maintenanceId)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getMaintenanceId(), getControlStandard());
	}
}