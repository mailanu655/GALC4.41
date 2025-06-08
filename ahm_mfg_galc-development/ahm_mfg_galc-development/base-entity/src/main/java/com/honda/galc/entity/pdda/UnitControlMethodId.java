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
public class UnitControlMethodId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="MAINTENANCE_ID", nullable=false)
	private int maintenanceId;

	@Column(name="CNTRL_METH_DESC", nullable=false, length=25)
	private String controlMethodDescription;

    public UnitControlMethodId() {}
    
	public int getMaintenanceId() {
		return this.maintenanceId;
	}
	
	public void setMaintenanceId(int maintenanceId) {
		this.maintenanceId = maintenanceId;
	}
	
	public String getControlMethodDescription() {
		return StringUtils.trim(this.controlMethodDescription);
	}
	
	public void setControlMethodDescription(String controlMethodDescription) {
		this.controlMethodDescription = controlMethodDescription;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((controlMethodDescription == null) ? 0 : controlMethodDescription.hashCode());
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
		UnitControlMethodId other = (UnitControlMethodId) obj;
		if (controlMethodDescription == null) {
			if (other.controlMethodDescription != null)
				return false;
		} else if (!controlMethodDescription.equals(other.controlMethodDescription))
			return false;
		if (maintenanceId != other.maintenanceId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getMaintenanceId(), getControlMethodDescription());
	}
}