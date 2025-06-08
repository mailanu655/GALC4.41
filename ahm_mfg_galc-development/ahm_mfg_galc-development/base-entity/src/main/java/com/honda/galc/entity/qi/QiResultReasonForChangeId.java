package com.honda.galc.entity.qi;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.honda.galc.util.ToStringUtil;

/**
 * 
 * <h3>QiResultReasonForChangeId Class description</h3>
 * <p>
 * QiResultReasonForChangeId description
 * </p>
 * 
 * <h4>Change History</h4>
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
 * @author Justin Jiang<br>
 *         November 3, 2020
 *
 */

@Embeddable
public class QiResultReasonForChangeId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "DEFECT_REPAIR_ID")
	private long defectRepairId;
	@Column(name = "DETAIL_ID")
	private int detailId;
	@Column(name = "IS_DEFECT_RESULT")
	private boolean isDefectResult;
	@Column(name = "CHANGE_TIMESTAMP")
	private Timestamp changeTimestamp;

	public QiResultReasonForChangeId() {
		super();
	}

	public long getDefectRepairId() {
		return defectRepairId;
	}

	public void setDefectRepairId(long defectRepairId) {
		this.defectRepairId = defectRepairId;
	}

	public int getDetailId() {
		return detailId;
	}

	public void setDetailId(int detailId) {
		this.detailId = detailId;
	}

	public boolean isDefectResult() {
		return isDefectResult;
	}

	public void setDefectResult(boolean isDefectResult) {
		this.isDefectResult = isDefectResult;
	}
	
	public Timestamp getChangeTimestamp() {
		return changeTimestamp;
	}

	public void setChangeTimestamp(Timestamp changeTimestamp) {
		this.changeTimestamp = changeTimestamp;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((changeTimestamp == null) ? 0 : changeTimestamp.hashCode());
		result = prime * result + (int) (defectRepairId ^ (defectRepairId >>> 32));
		result = prime * result + detailId;
		result = prime * result + (isDefectResult ? 1231 : 1237);
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
		QiResultReasonForChangeId other = (QiResultReasonForChangeId) obj;
		if (changeTimestamp == null) {
			if (other.changeTimestamp != null)
				return false;
		} else if (!changeTimestamp.equals(other.changeTimestamp))
			return false;
		if (defectRepairId != other.defectRepairId)
			return false;
		if (detailId != other.detailId)
			return false;
		if (isDefectResult != other.isDefectResult)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}
}
