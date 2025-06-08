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
public class UnitMostId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="MAINTENANCE_ID", nullable=false)
	private int maintenanceId;

	@Column(name="MOST_SEQ_NO", nullable=false)
	private short mostSeqNo;

	@Column(name="WORKING_POINT_DTL", nullable=false, length=254)
	private String workingPointDtl;
	
	@Column(name="SHOW_ON_PRINT", nullable=false, length=1)
	private String showOnPrint;

    public UnitMostId() {}
    
	public int getMaintenanceId() {
		return this.maintenanceId;
	}
	
	public void setMaintenanceId(int maintenanceId) {
		this.maintenanceId = maintenanceId;
	}
	
	public short getMostSeqNo() {
		return this.mostSeqNo;
	}
	
	public void setMostSeqNo(short mostSeqNo) {
		this.mostSeqNo = mostSeqNo;
	}
	
	public String getWorkingPointDtl() {
		return StringUtils.trim(this.workingPointDtl);
	}
	
	public void setWorkingPointDtl(String workingPointDtl) {
		this.workingPointDtl = workingPointDtl;
	}
	
	public String getShowOnPrint() {
		return StringUtils.trim(this.showOnPrint);
	}

	public void setShowOnPrint(String showOnPrint) {
		this.showOnPrint = showOnPrint;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + maintenanceId;
		result = prime * result + mostSeqNo;
		result = prime * result
				+ ((workingPointDtl == null) ? 0 : workingPointDtl.hashCode());
		result = prime * result
				+ ((showOnPrint == null) ? 0 : showOnPrint.hashCode());
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
		UnitMostId other = (UnitMostId) obj;
		if (maintenanceId != other.maintenanceId)
			return false;
		if (mostSeqNo != other.mostSeqNo)
			return false;
		if (workingPointDtl == null) {
			if (other.workingPointDtl != null)
				return false;
		} else if (!workingPointDtl.equals(other.workingPointDtl))
			return false;
		if (showOnPrint == null) {
			if (other.showOnPrint != null)
				return false;
		} else if (!showOnPrint.equals(other.showOnPrint))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getMaintenanceId(), getMostSeqNo(), getWorkingPointDtl(), getShowOnPrint());
	}
}