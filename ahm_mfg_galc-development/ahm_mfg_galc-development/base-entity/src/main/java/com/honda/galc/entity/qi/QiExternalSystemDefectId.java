package com.honda.galc.entity.qi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * <h3>QiDepartmentId Class description</h3>
 * <p>
 * QiDepartmentId contains the getter and setter of the Department composite key properties and maps this class with database and these columns .
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
 * @author vcc44349<br>
 * Oct 22, 2019
 * 
 */
@Embeddable
public class QiExternalSystemDefectId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "DEFECTRESULTID", nullable=false)
	private long defectResultId;
	
	@Column(name = "EXTERNAL_SYSTEM_ID")
	private short externalSystemId;
		
	@Column(name = "EXTERNAL_SYSTEM_DEFECT_KEY")
	private long externalSystemDefectKey;

	public QiExternalSystemDefectId() {
		super();
	}

	public QiExternalSystemDefectId(long defectResultId, short externalSystemId, long externalSystemDefectKey) {
		super();
		this.defectResultId = defectResultId;
		this.externalSystemId = externalSystemId;
		this.externalSystemDefectKey = externalSystemDefectKey;
	}

	public long getDefectResultId() {
		return defectResultId;
	}

	public void setDefectResultId(long defectResultId) {
		this.defectResultId = defectResultId;
	}

	public short getExternalSystemId() {
		return externalSystemId;
	}

	public void setExternalSystemId(short externalSystemId) {
		this.externalSystemId = externalSystemId;
	}

	public long getExternalSystemDefectKey() {
		return externalSystemDefectKey;
	}

	public void setExternalSystemDefectKey(long externalSystemDefectKey) {
		this.externalSystemDefectKey = externalSystemDefectKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (defectResultId ^ (defectResultId >>> 32));
		result = prime * result + (int) (externalSystemDefectKey ^ (externalSystemDefectKey >>> 32));
		result = prime * result + externalSystemId;
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
		QiExternalSystemDefectId other = (QiExternalSystemDefectId) obj;
		if (defectResultId != other.defectResultId)
			return false;
		if (externalSystemDefectKey != other.externalSystemDefectKey)
			return false;
		if (externalSystemId != other.externalSystemId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QiExternalSystemDefectId [defectResultId=" + defectResultId + ", externalSystemId=" + externalSystemId
				+ ", externalSystemDefectKey=" + externalSystemDefectKey + "]";
	}

}
