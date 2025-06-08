package com.honda.galc.entity.qi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.ToStringUtil;
/**
 * 
 * <h3>QiStationPreviousDefectId Class description</h3>
 * <p>
 *QiStationPreviousDefectId contains the getter and setter of the Entry department defect composite key properties and maps this class with database and these columns
 * </p>
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
 * @author LnTInfotech<br>
 *         10th April,2017
 * 
 */
@Embeddable
public class QiStationPreviousDefectId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "PROCESS_POINT_ID")
	private String processPointId;
	
	@Column(name = "ENTRY_DIVISION_ID")
	private String entryDivisionId;

	public String getProcessPointId() {
		return StringUtils.trimToEmpty(processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getEntryDivisionId() {
		return StringUtils.trimToEmpty(entryDivisionId);
	}

	public void setEntryDivisionId(String divisionId) {
		this.entryDivisionId = divisionId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entryDivisionId == null) ? 0 : entryDivisionId.hashCode());
		result = prime * result + ((processPointId == null) ? 0 : processPointId.hashCode());
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
		QiStationPreviousDefectId other = (QiStationPreviousDefectId) obj;
		if (entryDivisionId == null) {
			if (other.entryDivisionId != null)
				return false;
		} else if (!entryDivisionId.equals(other.entryDivisionId))
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}

}

