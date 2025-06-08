package com.honda.galc.entity.qi;

import java.io.File;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.util.CommonUtil;

/**
 * 
 * <h3>QiDefectResultImage Class description</h3>
 * <p>
 * QiDefectResultImage
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
@Table(name = "QI_DEFECT_RESULT_IMAGE_TBX")
public class QiDefectResultImage extends CreateUserAuditEntry implements Cloneable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private QiDefectResultImageId id;
	
	@Column(name = "APPLICATION_ID", nullable=false)
	private String applicationId;
	
	@Column(name = "ACTUAL_TIMESTAMP")
	private Date actualTimestamp;
	
	@Transient
	private File file;
	
	public QiDefectResultImage() {
		super();
	}

	public QiDefectResultImage(long defectResultId, String imageUrl) {
		super();
		this.id = new QiDefectResultImageId(defectResultId, imageUrl);
	}
	
	/**
	 * @return the id
	 */
	public QiDefectResultImageId getId() {
		return id;
	}

	public void setId(QiDefectResultImageId id) {
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

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
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
		QiDefectResultImage other = (QiDefectResultImage) obj;
		return this.id.equals(other.getId()) 
				&& this.actualTimestamp.equals(other.getActualTimestamp()) 
				&& StringUtils.equals(applicationId, other.getApplicationId());
	}
	
	@Override
	public String toString() {
		return toString(getId(), getApplicationId());
	}
}
