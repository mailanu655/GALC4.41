package com.honda.galc.entity.qi;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.util.CommonUtil;

/**
 * 
 * <h3>QiRepairResultImage Class description</h3>
 * <p>
 * QiRepairResultImage
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
 */
@Entity
@Table(name = "QI_REPAIR_RESULT_IMAGE_TBX")
public class QiRepairResultImage extends CreateUserAuditEntry implements Cloneable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private QiRepairResultImageId id;
	
	@Column(name = "APPLICATION_ID", nullable=false)
	private String applicationId;
	
	@Column(name = "ACTUAL_TIMESTAMP")
	private Date actualTimestamp;
	
	public QiRepairResultImage() {
		super();
	}

	public QiRepairResultImage(long repairResultId, String imageUrl) {
		super();
		this.id = new QiRepairResultImageId(repairResultId, imageUrl);
	}
	
	/**
	 * @return the id
	 */
	public QiRepairResultImageId getId() {
		return id;
	}

	public void setId(QiRepairResultImageId id) {
		this.id = id;
	}

	/**
	 * @return the applicationId
	 */
	public String getApplicationId() {
		return StringUtils.trimToEmpty(applicationId);
	}

	/**
	 * @param applicationId the applicationId to set
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}


	/**
	 * @return the actualTimestamp
	 */
	public Date getActualTimestamp() {
		return actualTimestamp;
	}

	public String getActualTimestampForDisplay() {
		return StringUtils.trimToEmpty(CommonUtil.formatDate(actualTimestamp));
	}

	/**
	 * @param entryTimestamp the actualTimestamp to set
	 */
	public void setActualTimestamp(Date actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
		result = prime * result + ((applicationId == null) ? 0 : applicationId.hashCode());
		result = prime * result + (id == null ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!this.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiRepairResultImage other = (QiRepairResultImage) obj;
		return this.id.equals(other.getId()) 
				&& this.actualTimestamp.equals(other.getActualTimestamp()) 
				&& StringUtils.equals(applicationId, other.getApplicationId());
	}
	
	@Override
	public String toString() {
		return toString(getId(), getApplicationId());
	}
}
