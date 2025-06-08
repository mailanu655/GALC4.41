package com.honda.galc.entity.qi;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.dto.DefectMapDto;
import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>QiExternalSystemData Class description</h3>
 * <p> QiExternalSystemData contains all error records that are not processed successfully </p>
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

@Entity
@Table(name = "QI_EXTERNAL_SYSTEM_DATA_TBX")
public class QiExternalSystemData extends AuditEntry  {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	@Auditable(isPartOfPrimaryKey = true)
	private QiExternalSystemDataId id;
	
	@Column(name = "ENTRY_SITE")
	private String entrySite;

	@Column(name = "ENTRY_DEPT")
	private String entryDept;
	
	@Column(name = "PRODUCT_TYPE")
	private String productType;

	@Column(name = "ORIGINAL_DEFECT_STATUS")
	private short originalDefectStatus;
	
	@Column(name = "CURRENT_DEFECT_STATUS")
	private short currentDefectStatus;
	
	@Column(name = "WRITE_UP_DEPT")
	private String writeUpDept;
	
	@Column(name = "IMAGE_NAME")
	private String imageName;
	
	@Column(name = "POINT_X")
	private int pointX;
	
	@Column(name = "POINT_Y")
	private int pointY;
	
	@Column(name = "ASSOCIATE_ID")
	private String associateId;
	
	@Column(name = "EXTERNAL_SYSTEM_DEFECT_KEY")
	private long externalSystemDefectKey;

	public QiExternalSystemData(){
		super();
	}

	public QiExternalSystemData(DefectMapDto defectMapDto){
		this.id = new QiExternalSystemDataId(
				defectMapDto.getExternalSystemName(),
				defectMapDto.getExternalPartCode(),
				defectMapDto.getExternalDefectCode(),
				defectMapDto.getProductId(),
				defectMapDto.getProcessPointId(),
				new Timestamp(System.currentTimeMillis()));
	}
	public QiExternalSystemDataId getId() {
		return id;
	}

	public void setId(QiExternalSystemDataId id) {
		this.id = id;
	}

	public String getEntrySite() {
		return StringUtils.trimToEmpty(this.entrySite);
	}

	public void setEntrySite(String entrySite) {
		this.entrySite = entrySite;
	}

	public String getEntryDept() {
		return StringUtils.trimToEmpty(this.entryDept);
	}

	public void setEntryDept(String entryDept) {
		this.entryDept = entryDept;
	}

	public String getProductType() {
		return StringUtils.trimToEmpty(this.productType);
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public short getOriginalDefectStatus() {
		return originalDefectStatus;
	}

	public void setOriginalDefectStatus(short originalDefectStatus) {
		this.originalDefectStatus = originalDefectStatus;
	}

	public short getCurrentDefectStatus() {
		return currentDefectStatus;
	}

	public void setCurrentDefectStatus(short currentDefectStatus) {
		this.currentDefectStatus = currentDefectStatus;
	}

	public String getWriteUpDept() {
		return StringUtils.trimToEmpty(this.writeUpDept);
	}

	public void setWriteUpDept(String writeUpDept) {
		this.writeUpDept = writeUpDept;
	}

	public String getImageName() {
		return StringUtils.trimToEmpty(this.imageName);
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public int getPointX() {
		return pointX;
	}

	public void setPointX(int pointX) {
		this.pointX = pointX;
	}

	public int getPointY() {
		return pointY;
	}

	public void setPointY(int pointY) {
		this.pointY = pointY;
	}

	public String getAssociateId() {
		return StringUtils.trimToEmpty(this.associateId);
	}

	public void setAssociateId(String associateId) {
		this.associateId = associateId;
	}

	
	
	
	
	public long getExternalSystemDefectKey() {
		return externalSystemDefectKey;
	}

	public void setExternalSystemDefectKey(long externalSystemDefectKey) {
		this.externalSystemDefectKey = externalSystemDefectKey;
	}

	@Override
	public String toString() {
		return "QiExternalSystemData [id=" + id + ", entrySite=" + entrySite
				+ ", entryDept=" + entryDept + ", productType=" + productType
				+ ", originalDefectStatus=" + originalDefectStatus
				+ ", currentDefectStatus=" + currentDefectStatus
				+ ", writeUpDept=" + writeUpDept + ", imageName=" + imageName
				+ ", pointX=" + pointX + ", pointY=" + pointY
				+ ", associateId=" + associateId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((associateId == null) ? 0 : associateId.hashCode());
		result = prime * result + currentDefectStatus;
		result = prime * result
				+ ((entryDept == null) ? 0 : entryDept.hashCode());
		result = prime * result
				+ ((entrySite == null) ? 0 : entrySite.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((imageName == null) ? 0 : imageName.hashCode());
		result = prime * result + originalDefectStatus;
		result = prime * result + pointX;
		result = prime * result + pointY;
		result = prime * result
				+ ((productType == null) ? 0 : productType.hashCode());
		result = prime * result
				+ ((writeUpDept == null) ? 0 : writeUpDept.hashCode());
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
		QiExternalSystemData other = (QiExternalSystemData) obj;
		if (associateId == null) {
			if (other.associateId != null)
				return false;
		} else if (!associateId.equals(other.associateId))
			return false;
		if (currentDefectStatus != other.currentDefectStatus)
			return false;
		if (entryDept == null) {
			if (other.entryDept != null)
				return false;
		} else if (!entryDept.equals(other.entryDept))
			return false;
		if (entrySite == null) {
			if (other.entrySite != null)
				return false;
		} else if (!entrySite.equals(other.entrySite))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imageName == null) {
			if (other.imageName != null)
				return false;
		} else if (!imageName.equals(other.imageName))
			return false;
		if (originalDefectStatus != other.originalDefectStatus)
			return false;
		if (pointX != other.pointX)
			return false;
		if (pointY != other.pointY)
			return false;
		if (productType == null) {
			if (other.productType != null)
				return false;
		} else if (!productType.equals(other.productType))
			return false;
		if (writeUpDept == null) {
			if (other.writeUpDept != null)
				return false;
		} else if (!writeUpDept.equals(other.writeUpDept))
			return false;
		return true;
	}
	
	
}
