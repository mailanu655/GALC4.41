package com.honda.galc.entity.mdrs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>QiDefectRespAssociate Class description</h3>
 * <p>
 * QiDefectRespAssociate contains the getter and setter of the responsible
 * associate and maps this class with database and these columns .
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
 *         May 15, 2017
 * 
 */
@Entity
@Table(name = "QI_DEFECT_RESP_ASSOCIATE_TBX", schema="VIOS")
public class QiDefectRespAssociate extends AuditEntry {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "QFS_DEFECT_NO")
	private int qfsDefectNo;

	@Column(name = "ASSOCIATE_ID")
	private String associateId;
	
	@Column(name = "STATUS")
	private short status;

	public Object getId() {
		return getQfsDefectNo();
	}

	public int getQfsDefectNo() {
		return qfsDefectNo;
	}

	public void setQfsDefectNo(int qfsDefectNo) {
		this.qfsDefectNo = qfsDefectNo;
	}

	public String getAssociateId() {
		return StringUtils.trimToEmpty(associateId);
	}

	public void setAssociateId(String associateId) {
		this.associateId = associateId;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((associateId == null) ? 0 : associateId.hashCode());
		result = prime * result + qfsDefectNo;
		result = prime * result + status;
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
		QiDefectRespAssociate other = (QiDefectRespAssociate) obj;
		if (associateId == null) {
			if (other.associateId != null)
				return false;
		} else if (!associateId.equals(other.associateId))
			return false;
		if (qfsDefectNo != other.qfsDefectNo)
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("QiDefectRespAssociate [qfsDefectNo=");
		builder.append(qfsDefectNo);
		builder.append(", associateId=");
		builder.append(associateId);
		builder.append(", status=");
		builder.append(status);
		builder.append("]");
		return builder.toString();
	}

}
