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
public class ProcessUnitId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="MAINTENANCE_ID", nullable=false)
	private int maintenanceId;

	@Column(name="UNIT_NO", nullable=false, length=6)
	private String unitNo;

	@Column(name="UNIT_SEQ_NO", nullable=false)
	private int unitSeqNo;

    public ProcessUnitId() {}
    
	public int getMaintenanceId() {
		return this.maintenanceId;
	}
	
	public void setMaintenanceId(int maintenanceId) {
		this.maintenanceId = maintenanceId;
	}
	
	public String getUnitNo() {
		return StringUtils.trim(this.unitNo);
	}
	
	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}
	
	public int getUnitSeqNo() {
		return this.unitSeqNo;
	}
	
	public void setUnitSeqNo(int unitSeqNo) {
		this.unitSeqNo = unitSeqNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + maintenanceId;
		result = prime * result + ((unitNo == null) ? 0 : unitNo.hashCode());
		result = prime * result + unitSeqNo;
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
		ProcessUnitId other = (ProcessUnitId) obj;
		if (maintenanceId != other.maintenanceId)
			return false;
		if (unitNo == null) {
			if (other.unitNo != null)
				return false;
		} else if (!unitNo.equals(other.unitNo))
			return false;
		if (unitSeqNo != other.unitSeqNo)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getMaintenanceId(), 
				getUnitNo(), getUnitSeqNo());
	}

}