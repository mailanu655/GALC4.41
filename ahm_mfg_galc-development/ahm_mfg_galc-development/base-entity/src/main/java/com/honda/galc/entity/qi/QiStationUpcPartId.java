package com.honda.galc.entity.qi;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.ToStringUtil;

/**
 * 
 * <h3>QiStationUpcPartId Class description</h3>
 * <p> QiStationUpcPartId contains the getter and setter of the station UPC Part composite key properties and maps this class with database and these columns .
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
 *        Jan 05, 2016
 * 
 */
@Embeddable
public class QiStationUpcPartId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "PROCESS_POINT_ID", nullable=false)
	private String processPointId;

	@Column(name = "MAIN_PART_NO", nullable=false)
	private String mainPartNo;

	

	public QiStationUpcPartId() {}

	public QiStationUpcPartId(String processPointId, String mainPartNo) {
		this.setProcessPointId(processPointId);
		this.setMainPartNo(mainPartNo);
	}

	/**
	 * @return the processPointId
	 */
	public String getProcessPointId() {
		return StringUtils.trimToEmpty(processPointId);
	}

	/**
	 * @param processPointId the processPointId to set
	 */
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	/**
	 * @return the mainPartNo
	 */
	public String getMainPartNo() {
		return StringUtils.trimToEmpty(mainPartNo);
	}

	/**
	 * @param mainPartNo the mainPartNo to set
	 */
	public void setMainPartNo(String mainPartNo) {
		this.mainPartNo = mainPartNo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mainPartNo == null) ? 0 : mainPartNo.hashCode());
		result = prime * result
				+ ((processPointId == null) ? 0 : processPointId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiStationUpcPartId other = (QiStationUpcPartId) obj;
		if (mainPartNo == null) {
			if (other.mainPartNo != null)
				return false;
		} else if (!mainPartNo.equals(other.mainPartNo))
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
