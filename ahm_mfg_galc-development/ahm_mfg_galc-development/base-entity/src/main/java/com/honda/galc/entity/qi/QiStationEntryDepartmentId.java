package com.honda.galc.entity.qi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.ToStringUtil;


/**
 * 
 * <h3>QiStationEntryDepartmentId Class description</h3>
 * <p>
 * QiStationEntryDepartmentId contains the getter and setter of the Department composite key properties and maps this class with database and these columns .
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
 * @author LnTInfotech<br>
 * 
 */
@Embeddable
public class QiStationEntryDepartmentId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "PROCESS_POINT_ID")
	private String processPointId;
	
	@Column(name = "DIVISION_ID")
	private String divisionId;

	public String getProcessPointId() {
		return StringUtils.trimToEmpty(processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getDivisionId() {
		return StringUtils.trimToEmpty(divisionId);
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((divisionId == null) ? 0 : divisionId.hashCode());
		result = prime * result
				+ ((processPointId == null) ? 0 : processPointId.hashCode());
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
		QiStationEntryDepartmentId other = (QiStationEntryDepartmentId) obj;
		if (divisionId == null) {
			if (other.divisionId != null)
				return false;
		} else if (!divisionId.equals(other.divisionId))
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
