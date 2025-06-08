package com.honda.galc.entity.product;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.util.CommonUtil;

@Entity
@Table(name = "GAL185_HIST_TBX")
public class InstalledPartHistory extends AuditEntry {
	@EmbeddedId
	private InstalledPartHistoryId id;

	private static final long serialVersionUID = 1L;

	@Column(name = "PART_SERIAL_NUMBER")
	private String partSerialNumber;

	@Column(name = "PASS_TIME")
	private int passTime;

	@Column(name = "INSTALLED_PART_REASON")
	private String installedPartReason;

	@Column(name = "FIRST_ALARM")
	private int firstAlarm;

	@Column(name = "SECOND_ALARM")
	private int secondAlarm;

	@Column(name = "PART_ID", length=5)
	private String partId;
	
	@Column(name = "PART_REV")
	private int partRevision;
	
	@Column(name = "OP_REV")
	private int operationRevision;

	@Column(name = "INSTALLED_PART_STATUS")
	private Integer installedPartStatusId;

	@Column(name = "ASSOCIATE_NO")
	private String associateNo;
	
	@Column(name = "PROCESS_POINT_ID")
	private String processPointId;
	
	@Column(name = "FEATURE_TYPE")
    private String featureType;
    
    @Column(name = "FEATURE_ID")
    private String featureId;

    @Column(name = "PRODUCT_TYPE")
    private String productType;
    
    @Column(name = "OVERRIDE_ASSOCIATE_NO")
	private String overrideAssociateNo;
	
    
	public InstalledPartHistory() {
		super();
	}

	public InstalledPartHistory Initialize(InstalledPart installedPart) {

		InstalledPartHistory history = new InstalledPartHistory();
		InstalledPartHistoryId historyId = new InstalledPartHistoryId();
		historyId.setProductId(installedPart.getId().getProductId());
		historyId.setPartName(installedPart.getPartName());
		historyId.setActualTimestamp((installedPart.getActualTimestamp() == null) ? (new Timestamp(
						System.currentTimeMillis()))
						: installedPart.getActualTimestamp());
		history.setId(historyId);
		history.setPartId(installedPart.getPartId());
		history.setPartRevision(installedPart.getPartRevision());
		history.setOperationRevision(installedPart.getOperationRevision());
		history.setPartSerialNumber(installedPart.getPartSerialNumber());
		history.setPassTime(installedPart.getPassTime());
		history.setInstalledPartReason(installedPart.getInstalledPartReason());
		history.setAssociateNo(installedPart.getAssociateNo());
		history.setInstalledPartStatusId(installedPart.getInstalledPartStatusId());
		history.setFirstAlarm(installedPart.getFirstAlarm());
		history.setSecondAlarm(installedPart.getSecondAlarm());
		history.setProcessPointId(installedPart.getProcessPointId());
		history.setFeatureId(installedPart.getFeatureId());
		history.setFeatureType(installedPart.getFeatureType());
		history.setProductType(installedPart.getProductType());
		history.setOverrideAssociateNo(installedPart.getOverrideAssociateNo());
		return history;
	}

	public InstalledPartHistoryId getId() {
		return id;
	}

	public void setId(InstalledPartHistoryId id) {
		this.id = id;
	}

	public String getPartSerialNumber() {
		return partSerialNumber;
	}

	public void setPartSerialNumber(String partSerialNumber) {
		this.partSerialNumber = partSerialNumber;
	}

	public int getPassTime() {
		return passTime;
	}

	public void setPassTime(int passTime) {
		this.passTime = passTime;
	}

	public String getInstalledPartReason() {
		return installedPartReason;
	}

	public void setInstalledPartReason(String installedPartReason) {
		this.installedPartReason = installedPartReason;
	}

	public int getFirstAlarm() {
		return firstAlarm;
	}

	public void setFirstAlarm(int firstAlarm) {
		this.firstAlarm = firstAlarm;
	}

	public int getSecondAlarm() {
		return secondAlarm;
	}

	public void setSecondAlarm(int secondAlarm) {
		this.secondAlarm = secondAlarm;
	}

	public String getPartId() {
		return StringUtils.trim(partId);
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	public int getPartRevision() {
		return partRevision;
	}

	public void setPartRevision(int partRevision) {
		this.partRevision = partRevision;
	}
	
	public int getOperationRevision() {
		return operationRevision;
	}

	public void setOperationRevision(int operationRevision) {
		this.operationRevision = operationRevision;
	}

	public Integer getInstalledPartStatusId() {
		return installedPartStatusId;
	}

	public void setInstalledPartStatusId(Integer installedPartStatusId) {
		this.installedPartStatusId = installedPartStatusId;
	}

	public String getAssociateNo() {
		return associateNo;
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}
	
	public String getProcessPointId() {
		return processPointId;
	}
	
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	
	public String getFeatureType() {
		return featureType;
	}

	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}


	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getProductType() {
		return productType;
	}
	
	public String getOverrideAssociateNo() {
		return StringUtils.trim(overrideAssociateNo);
	}

	public void setOverrideAssociateNo(String overrideAssociateNo) {
		this.overrideAssociateNo = overrideAssociateNo;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((associateNo == null) ? 0 : associateNo.hashCode());
		result = prime * result
				+ ((featureId == null) ? 0 : featureId.hashCode());
		result = prime * result
				+ ((featureType == null) ? 0 : featureType.hashCode());
		result = prime * result + firstAlarm;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((installedPartReason == null) ? 0 : installedPartReason
						.hashCode());
		result = prime
				* result
				+ ((installedPartStatusId == null) ? 0 : installedPartStatusId
						.hashCode());
		result = prime * result + ((partId == null) ? 0 : partId.hashCode());
		result = prime * result + partRevision;
		result = prime * result + operationRevision;
		result = prime
				* result
				+ ((partSerialNumber == null) ? 0 : partSerialNumber.hashCode());
		result = prime * result + passTime;
		result = prime * result
				+ ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result + secondAlarm;
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
		InstalledPartHistory other = (InstalledPartHistory) obj;
		if (associateNo == null) {
			if (other.associateNo != null)
				return false;
		} else if (!associateNo.equals(other.associateNo))
			return false;
		if (featureId == null) {
			if (other.featureId != null)
				return false;
		} else if (!featureId.equals(other.featureId))
			return false;
		if (featureType == null) {
			if (other.featureType != null)
				return false;
		} else if (!featureType.equals(other.featureType))
			return false;
		if (firstAlarm != other.firstAlarm)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (installedPartReason == null) {
			if (other.installedPartReason != null)
				return false;
		} else if (!installedPartReason.equals(other.installedPartReason))
			return false;
		if (installedPartStatusId == null) {
			if (other.installedPartStatusId != null)
				return false;
		} else if (!installedPartStatusId.equals(other.installedPartStatusId))
			return false;
		if (partId == null) {
			if (other.partId != null)
				return false;
		} else if (!partId.equals(other.partId))
			return false;
		if (partRevision != other.partRevision)
			return false;
		if (operationRevision != other.operationRevision)
			return false;
		if (partSerialNumber == null) {
			if (other.partSerialNumber != null)
				return false;
		} else if (!partSerialNumber.equals(other.partSerialNumber))
			return false;
		if (passTime != other.passTime)
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		if (secondAlarm != other.secondAlarm)
			return false;
		return true;
	}

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"").append(getId().getProductId()).append("\"");
        sb.append(",\"").append(getId().getPartName()).append("\"");
        sb.append(",\"").append(CommonUtil.convertNull(partSerialNumber)).append("\"");
        sb.append(",").append(passTime);
        sb.append(",\"").append(CommonUtil.convertNull(installedPartReason)).append("\"");
        sb.append(",\"").append(CommonUtil.convertNull(getProcessPointId())).append("\"");
        sb.append(",\"").append(CommonUtil.convertNull(getAssociateNo())).append("\"");
        sb.append(",\"").append(CommonUtil.format(getId().getActualTimestamp())).append("\"");
        sb.append(",").append(getInstalledPartStatusId());
        sb.append(",").append(firstAlarm);
        sb.append(",").append(secondAlarm);
        sb.append(",\"").append(CommonUtil.convertNull(featureId)).append("\"");
        sb.append(",\"").append(CommonUtil.convertNull(featureType)).append("\"");
        sb.append(",\"").append(CommonUtil.convertNull(partId)).append("\"");
        sb.append(",\"").append(partRevision).append("\"");
        sb.append(",\"").append(operationRevision).append("\"");
        sb.append(",\"").append(CommonUtil.format(getCreateTimestamp())).append("\"");
        sb.append(",\"").append(CommonUtil.format(getUpdateTimestamp())).append("\"");
        sb.append(",\"").append(CommonUtil.convertNull(getOverrideAssociateNo())).append("\"");
        
      return sb.toString();
	}
	
}
