package com.honda.galc.entity.conf;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumn;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumns;


import com.honda.galc.constant.PartType;
import com.honda.galc.constant.PartValidity;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.PartCheck;

/**
 * @author Subu Kathiresan
 * Feb 10, 2014
 */
@Entity
@Table(name="MC_OP_PART_REV_TBX")
public class MCOperationPartRevision extends AuditEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MCOperationPartRevisionId id;

	@Column(name="APPROVED")
	private Date approved;

	@Column(name="DEPRECATED")
	private Date deprecated;

	@Column(name="DEVICE_ID", length=32)
	private String deviceId;

	@Column(name="DEVICE_MSG", length=32)
	private String deviceMsg;

	@Column(name="MEASUREMENT_COUNT")
	private int measCount;

	@Column(name="PART_CHECK")
	 @Enumerated(EnumType.ORDINAL)
	private PartCheck partCheck;

	@Column(name="PART_DESC", length=255)
	private String partDesc;

	@Column(name="PART_ITEM_NO", length=8)
	private String partItemNo;

	@Column(name="PART_MARK", length=32)
	private String partMark;

	@Column(name="PART_MASK", length=255)
	private String partMask;

	@Column(name="PART_MAX_ATTEMPTS")
	private int partMaxAttempts;

	@Column(name="PART_NO", length=18)
	private String partNo;

	@Column(name="PART_PROCESSOR", length=255)
	private String partProcessor;

	@Column(name="PART_SECTION_CODE", length=3)
	private String partSectionCode;

	@Column(name="PART_TIME")
	private int partTime;

	@Column(name="PART_TYPE", length=32)
    @Enumerated(EnumType.STRING)
	private PartType partType;

	@Column(name="PART_VIEW", length=255)
	private String partView;

	@Column(name="REV_ID")
	private long revisionId;
	
	@Column(name="DEPRECATED_REV_ID")
	private long deprecatedRevisionId;
	
	@OneToMany(fetch=FetchType.EAGER)
	 @ElementJoinColumns({
		 @ElementJoinColumn(name="OPERATION_NAME", referencedColumnName="OPERATION_NAME", updatable = false, insertable=false),
		 @ElementJoinColumn(name="PART_ID", referencedColumnName="PART_ID", updatable = false, insertable=false),
		 @ElementJoinColumn(name="PART_REV", referencedColumnName="PART_REV", updatable = false, insertable=false)	 
	 })	
	@OrderBy
	private List<MCOperationMeasurement> measurements;
	
	@Transient 
	private PartValidity partValidity;

    public MCOperationPartRevision() {}

	public MCOperationPartRevisionId getId() {
		return this.id;
	}

	public void setId(MCOperationPartRevisionId id) {
		this.id = id;
	}
	
	public Date getApproved() {
		return this.approved;
	}

	public void setApproved(Timestamp approved) {
		this.approved = approved;
	}

	public Date getDeprecated() {
		return this.deprecated;
	}

	public void setDeprecated(Timestamp deprecated) {
		this.deprecated = deprecated;
	}

	public String getDeviceId() {
		return StringUtils.trim(this.deviceId);
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceMsg() {
		return StringUtils.trim(this.deviceMsg);
	}

	public void setDeviceMsg(String deviceMsg) {
		this.deviceMsg = deviceMsg;
	}

	public int getMeasCount() {
		return measCount;
	}

	public void setMeasCount(int measCount) {
		this.measCount = measCount;
	}

	public int getMeasurementCount() {
		if (measurements != null) {
			return measurements.size();
		}
		return 0;
	}
	
	public PartCheck getPartCheck() {
		return this.partCheck;
	}

	public void setPartCheck(PartCheck partCheck) {
		this.partCheck = partCheck;
	}

	public String getPartDesc() {
		return StringUtils.trim(this.partDesc);
	}

	public void setPartDesc(String partDesc) {
		this.partDesc = partDesc;
	}

	public String getPartItemNo() {
		return StringUtils.trim(this.partItemNo);
	}

	public void setPartItemNo(String partItemNo) {
		this.partItemNo = partItemNo;
	}

	public String getPartMark() {
		return StringUtils.trim(this.partMark);
	}

	public void setPartMark(String partMark) {
		this.partMark = partMark;
	}

	public String getPartMask() {
		return StringUtils.trim(this.partMask);
	}

	public void setPartMask(String partMask) {
		this.partMask = partMask;
	}

	public int getPartMaxAttempts() {
		return this.partMaxAttempts;
	}

	public void setPartMaxAttempts(int partMaxAttempts) {
		this.partMaxAttempts = partMaxAttempts;
	}

	public String getPartNo() {
		return StringUtils.trim(this.partNo);
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public String getPartProcessor() {
		return StringUtils.trim(this.partProcessor);
	}

	public void setPartProcessor(String partProcessor) {
		this.partProcessor = partProcessor;
	}

	public String getPartSectionCode() {
		return StringUtils.trim(this.partSectionCode);
	}

	public void setPartSectionCode(String partSectionCode) {
		this.partSectionCode = partSectionCode;
	}

	public int getPartTime() {
		return this.partTime;
	}

	public void setPartTime(int partTime) {
		this.partTime = partTime;
	}

	public PartType getPartType() {
		return partType;
	}

	public void setPartType(PartType partType) {
		this.partType = partType;
	}

	public String getPartView() {
		return StringUtils.trim(this.partView);
	}

	public void setPartView(String partView) {
		this.partView = partView;
	}

	public long getRevisionId() {
		return this.revisionId;
	}

	public void setRevisionId(long revisionId) {
		this.revisionId = revisionId;
	}
	
	public void setDeprecatedRevisionId(long deprecatedRevisionId) {
		this.deprecatedRevisionId = deprecatedRevisionId;
	}

	public long getDeprecatedRevisionId() {
		return deprecatedRevisionId;
	}
	
	public List<MCOperationMeasurement> getMeasurements() {
		return this.measurements;
	}

	public void setMeasurements(List<MCOperationMeasurement> measurements) {
		this.measurements = measurements;
	}
	
	public boolean hasMeasurements() {
		return measurements != null && !measurements.isEmpty();
	}

	public String getDeviceId(int measurementIndex) {
		MCOperationMeasurement measurement = getMeasurement(measurementIndex);
		if(measurement == null) return null;
		return StringUtils.isEmpty(measurement.getDeviceId()) ? getDeviceId() : measurement.getDeviceId(); 
	}
	
	public String getDeviceMsg(int measurementIndex) {
		MCOperationMeasurement measurement = getMeasurement(measurementIndex);
		if(measurement == null) return null;
		return StringUtils.isEmpty(measurement.getDeviceMsg()) ? getDeviceMsg() : measurement.getDeviceMsg(); 
	}
	
	public MCOperationMeasurement getMeasurement(int measurementIndex) {
		if(!hasMeasurements() || measurementIndex <0 || measurementIndex >= measurements.size()) return null;
		return measurements.get(measurementIndex);
	}
	
	public PartValidity getPartValidity() {
		return partValidity;
	}

	public void setPartValidity(PartValidity partValidity) {
		this.partValidity = partValidity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((approved == null) ? 0 : approved.hashCode());
		result = prime * result
				+ ((deprecated == null) ? 0 : deprecated.hashCode());
		result = prime * result
				+ (int) (deprecatedRevisionId ^ (deprecatedRevisionId >>> 32));
		result = prime * result
				+ ((deviceId == null) ? 0 : deviceId.hashCode());
		result = prime * result
				+ ((deviceMsg == null) ? 0 : deviceMsg.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + measCount;
		result = prime * result
				+ ((measurements == null) ? 0 : measurements.hashCode());
		result = prime * result + partCheck.ordinal();
		result = prime * result
				+ ((partDesc == null) ? 0 : partDesc.hashCode());
		result = prime * result
				+ ((partItemNo == null) ? 0 : partItemNo.hashCode());
		result = prime * result
				+ ((partMark == null) ? 0 : partMark.hashCode());
		result = prime * result
				+ ((partMask == null) ? 0 : partMask.hashCode());
		result = prime * result + partMaxAttempts;
		result = prime * result + ((partNo == null) ? 0 : partNo.hashCode());
		result = prime * result
				+ ((partProcessor == null) ? 0 : partProcessor.hashCode());
		result = prime * result
				+ ((partSectionCode == null) ? 0 : partSectionCode.hashCode());
		result = prime * result + partTime;
		result = prime * result
				+ ((partType == null) ? 0 : partType.hashCode());
		result = prime * result
				+ ((partView == null) ? 0 : partView.hashCode());
		result = prime * result + (int) (revisionId ^ (revisionId >>> 32));
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
		MCOperationPartRevision other = (MCOperationPartRevision) obj;
		if (approved == null) {
			if (other.approved != null)
				return false;
		} else if (!approved.equals(other.approved))
			return false;
		if (deprecated == null) {
			if (other.deprecated != null)
				return false;
		} else if (!deprecated.equals(other.deprecated))
			return false;
		if (deprecatedRevisionId != other.deprecatedRevisionId)
			return false;
		if (deviceId == null) {
			if (other.deviceId != null)
				return false;
		} else if (!deviceId.equals(other.deviceId))
			return false;
		if (deviceMsg == null) {
			if (other.deviceMsg != null)
				return false;
		} else if (!deviceMsg.equals(other.deviceMsg))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (measCount != other.measCount)
			return false;
		if (measurements == null) {
			if (other.measurements != null)
				return false;
		} else if (!measurements.equals(other.measurements))
			return false;
		if (partCheck != other.partCheck)
			return false;
		if (partDesc == null) {
			if (other.partDesc != null)
				return false;
		} else if (!partDesc.equals(other.partDesc))
			return false;
		if (partItemNo == null) {
			if (other.partItemNo != null)
				return false;
		} else if (!partItemNo.equals(other.partItemNo))
			return false;
		if (partMark == null) {
			if (other.partMark != null)
				return false;
		} else if (!partMark.equals(other.partMark))
			return false;
		if (partMask == null) {
			if (other.partMask != null)
				return false;
		} else if (!partMask.equals(other.partMask))
			return false;
		if (partMaxAttempts != other.partMaxAttempts)
			return false;
		if (partNo == null) {
			if (other.partNo != null)
				return false;
		} else if (!partNo.equals(other.partNo))
			return false;
		if (partProcessor == null) {
			if (other.partProcessor != null)
				return false;
		} else if (!partProcessor.equals(other.partProcessor))
			return false;
		if (partSectionCode == null) {
			if (other.partSectionCode != null)
				return false;
		} else if (!partSectionCode.equals(other.partSectionCode))
			return false;
		if (partTime != other.partTime)
			return false;
		if (partType == null) {
			if (other.partType != null)
				return false;
		} else if (!partType.equals(other.partType))
			return false;
		if (partView == null) {
			if (other.partView != null)
				return false;
		} else if (!partView.equals(other.partView))
			return false;
		if (revisionId != other.revisionId)
			return false;
		return true;
	}

	@Override
	public String toString(){
		return toString(getId().getOperationName(), getId().getPartId(), getId().getPartRevision(), getApproved(), getDeprecated(),
				getDeviceId(), getDeviceMsg(), getMeasCount(), getPartCheck(), getPartDesc(), getPartItemNo(),
				getPartMark(), getPartMask(), getPartMaxAttempts(), getPartNo(), getPartProcessor(), getPartSectionCode(),
				getPartTime(), getPartType(), getPartView(), getRevisionId(), getDeprecatedRevisionId());
	}
}