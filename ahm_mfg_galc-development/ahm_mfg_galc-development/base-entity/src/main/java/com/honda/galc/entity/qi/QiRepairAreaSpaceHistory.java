package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;

/**
 * 
 * <h3>QiRepairAreaSpaceHistory Class description</h3>
 * <p> QiRepairAreaSpaceHistory description </p>
 * 
 * <h4>QiRepairAreaSpaceHistory History data for repair area space.</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 * 
 * @author LnTInfotech<br>
 *        Mar 1, 2017
 * 
 */

@Entity
@Table(name = "QI_REPAIR_AREA_SPACE_HIST_TBX")
public class QiRepairAreaSpaceHistory extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;

	@Id
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private QiRepairAreaSpaceHistoryId id;
	
	
	@Column(name = "DEFECTRESULTID")
	@Auditable(isPartOfPrimaryKey = false, sequence = 2)
	private Long defectResultId;
	
	@Column(name = "TARGET_RESP_DEPT")
	private String targetRespDept;
	
	@Column(name = "TARGET_REPAIR_AREA")
	private String targetRepairArea;
	
	public Long getDefectResultId() {
		return defectResultId;
	}


	public void setDefectResultId(Long defectResultId) {
		this.defectResultId = defectResultId;
	}


	public void setId(QiRepairAreaSpaceHistoryId id) {
		this.id = id;
	}


	public QiRepairAreaSpaceHistoryId getId() {
		return this.id;
	}


	public String getTargetRespDept() {
		return targetRespDept;
	}


	public void setTargetRespDept(String targetRespDept) {
		this.targetRespDept = targetRespDept;
	}


	public String getTargetRepairArea() {
		return targetRepairArea;
	}


	public void setTargetRepairArea(String targetRepairArea) {
		this.targetRepairArea = targetRepairArea;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((defectResultId == null) ? 0 : defectResultId.hashCode());
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
		QiRepairAreaSpaceHistory other = (QiRepairAreaSpaceHistory) obj;
		if (defectResultId == null) {
			if (other.defectResultId != null)
				return false;
		} else if (!defectResultId.equals(other.defectResultId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "QiRepairAreaSpaceHistory [id=" + id + ", defectResultId=" + defectResultId + "]";
	}
	
	
}
