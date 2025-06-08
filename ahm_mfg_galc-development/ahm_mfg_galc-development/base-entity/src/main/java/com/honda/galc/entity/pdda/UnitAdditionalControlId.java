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
public class UnitAdditionalControlId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="MAINTENANCE_ID", nullable=false)
	private int maintenanceId;

	@Column(name="CONTROL_METHOD", nullable=false, length=30)
	private String controlMethod;

    public UnitAdditionalControlId() {}
    
	public int getMaintenanceId() {
		return this.maintenanceId;
	}
	
	public void setMaintenanceId(int maintenanceId) {
		this.maintenanceId = maintenanceId;
	}
	
	public String getControlMethod() {
		return StringUtils.trim(this.controlMethod);
	}
	
	public void setControlMethod(String controlMethod) {
		this.controlMethod = controlMethod;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((controlMethod == null) ? 0 : controlMethod.hashCode());
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
		UnitAdditionalControlId other = (UnitAdditionalControlId) obj;
		if (controlMethod == null) {
			if (other.controlMethod != null)
				return false;
		} else if (!controlMethod.equals(other.controlMethod))
			return false;
		if (maintenanceId != other.maintenanceId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getMaintenanceId(), getControlMethod());
	}
}