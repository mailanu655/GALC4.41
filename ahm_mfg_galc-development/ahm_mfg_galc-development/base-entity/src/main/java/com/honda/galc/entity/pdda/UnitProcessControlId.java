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
public class UnitProcessControlId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="MAINTENANCE_ID", nullable=false)
	private int maintenanceId;

	@Column(name="CONTROL_METHOD", nullable=false, length=30)
	private String controlMethod;

    public UnitProcessControlId() {}
    
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
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof UnitProcessControlId)) {
			return false;
		}
		UnitProcessControlId castOther = (UnitProcessControlId)other;
		return 
			(this.maintenanceId == castOther.maintenanceId)
			&& this.controlMethod.equals(castOther.controlMethod);
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.maintenanceId;
		hash = hash * prime + this.controlMethod.hashCode();
		
		return hash;
    }
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getMaintenanceId(), getControlMethod());
	}
}