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
public class UnitQualityIssueId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="MAINTENANCE_ID", nullable=false)
	private int maintenanceId;

	@Column(name="QUALITY_ISS_DESC", nullable=false, length=20)
	private String qualityIssDesc;

    public UnitQualityIssueId() {}
    
	public int getMaintenanceId() {
		return this.maintenanceId;
	}
	
	public void setMaintenanceId(int maintenanceId) {
		this.maintenanceId = maintenanceId;
	}
	
	public String getQualityIssDesc() {
		return StringUtils.trim(this.qualityIssDesc);
	}
	
	public void setQualityIssDesc(String qualityIssDesc) {
		this.qualityIssDesc = qualityIssDesc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + maintenanceId;
		result = prime * result
				+ ((qualityIssDesc == null) ? 0 : qualityIssDesc.hashCode());
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
		UnitQualityIssueId other = (UnitQualityIssueId) obj;
		if (maintenanceId != other.maintenanceId)
			return false;
		if (qualityIssDesc == null) {
			if (other.qualityIssDesc != null)
				return false;
		} else if (!qualityIssDesc.equals(other.qualityIssDesc))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getMaintenanceId(), getQualityIssDesc());
	}
}