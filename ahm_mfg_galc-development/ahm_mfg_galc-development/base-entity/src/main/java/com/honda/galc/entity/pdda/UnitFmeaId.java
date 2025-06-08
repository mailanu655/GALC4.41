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
public class UnitFmeaId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="MAINTENANCE_ID", nullable=false)
	private int maintenanceId;

	@Column(name="QUALITY_POINTS", nullable=false, length=250)
	private String qualityPoints;

	@Column(name="WORKING_POINTS", nullable=false, length=250)
	private String workingPoints;

    public UnitFmeaId() {}
    
	public int getMaintenanceId() {
		return this.maintenanceId;
	}
	
	public void setMaintenanceId(int maintenanceId) {
		this.maintenanceId = maintenanceId;
	}
	
	public String getQualityPoints() {
		return StringUtils.trim(this.qualityPoints);
	}
	
	public void setQualityPoints(String qualityPoints) {
		this.qualityPoints = qualityPoints;
	}
	
	public String getWorkingPoints() {
		return StringUtils.trim(this.workingPoints);
	}
	
	public void setWorkingPoints(String workingPoints) {
		this.workingPoints = workingPoints;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + maintenanceId;
		result = prime * result
				+ ((qualityPoints == null) ? 0 : qualityPoints.hashCode());
		result = prime * result
				+ ((workingPoints == null) ? 0 : workingPoints.hashCode());
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
		UnitFmeaId other = (UnitFmeaId) obj;
		if (maintenanceId != other.maintenanceId)
			return false;
		if (qualityPoints == null) {
			if (other.qualityPoints != null)
				return false;
		} else if (!qualityPoints.equals(other.qualityPoints))
			return false;
		if (workingPoints == null) {
			if (other.workingPoints != null)
				return false;
		} else if (!workingPoints.equals(other.workingPoints))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getMaintenanceId(), getQualityPoints(), getWorkingPoints());
	}
}