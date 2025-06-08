package com.honda.galc.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.product.Kickout;
import com.honda.galc.entity.product.KickoutLocation;

public class KickoutDto implements IDto, Serializable {
	private static final long serialVersionUID = 1L;
	
	@DtoTag(outputName = "KICKOUT_ID")
	private long kickoutId;
	
	@DtoTag(outputName = "PRODUCT_ID")
	private String productId;
	
	@DtoTag(outputName = "MC_SERIAL_NUMBER")
	private String mcSerialNumber;
	
	@DtoTag(outputName = "DC_SERIAL_NUMBER")
	private String dcSerialNumber;
	
	@DtoTag(outputName = "PRODUCT_SPEC_CODE")
	private String productSpecCode;
	
	@DtoTag(outputName = "DUNNAGE")
	private String dunnage;
	
	@DtoTag(outputName = "LAST_PASSING_PROCESS_POINT_ID")
	private String lastPassingProcessPointId;
	
	@DtoTag(outputName = "DESCRIPTION")
	private String description;
	
	@DtoTag(outputName = "KICKOUT_STATUS")
	private int kickoutStatus;
	
	@DtoTag(outputName = "COMMENT")
	private String comment;
	
	@DtoTag(outputName = "RELEASE_COMMENT")
	private String releaseComment;
	
	@DtoTag(outputName = "APPROVER_NAME")
	private String approverName;
	
	@DtoTag(outputName = "PRODUCT_TYPE")
	private String productType;
	
	@DtoTag(outputName = "KICKOUT_USER")
	private String kickoutUser;
	
	@DtoTag (outputName = "RELEASE_USER")
	private String releaseUser;
	
	@DtoTag(outputName = "DIVISION_ID")
	private String divisionId;
	
	@DtoTag(outputName = "LINE_ID")
	private String lineId;
	
	@DtoTag(outputName = "PROCESS_POINT_ID")
	private String processPointId;
	
	@DtoTag(outputName = "DIVISION_NAME")
	private String divisionName;
	
	@DtoTag(outputName = "LINE_NAME")
	private String LineName;

	@DtoTag(outputName = "PROCESS_POINT_NAME")
	private String processPointName;
	
	@DtoTag(outputName = "CREATE_TIMESTAMP")
	private Timestamp createTimestamp;

	private String lastPassingProcessPointName;
	
	private String kickoutStatusName;
	
	private int divisionSequence;
	
	private int lineSequence;
	
	private int processPointSequence;

	public long getKickoutId() {
		return this.kickoutId;
	}

	public void setKickoutId(long kickoutId) {
		this.kickoutId = kickoutId;
	}

	public String getProductId() {
		return StringUtils.trim(this.productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getMcSerialNumber() {
		return StringUtils.trim(this.mcSerialNumber);
	}
	
	public void setMcSerialNumber(String mcSerialNumber) {
		this.mcSerialNumber = mcSerialNumber;
	}
	
	public String getDcSerialNumber() {
		return StringUtils.trim(this.dcSerialNumber);
	}
	
	public void setDcSerialNumber(String dcSerialNumber) {
		this.dcSerialNumber = dcSerialNumber;
	}
	public String getProductSpecCode() {
		return StringUtils.trim(this.productSpecCode);
	}
	
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
	
	public String getDunnage() {
		return StringUtils.trim(this.dunnage);
	}
	
	public void setDunnage(String dunnage) {
		this.dunnage = dunnage;
	}
	
	public String getLastPassingProcessPointId() {
		return StringUtils.trim(this.lastPassingProcessPointId);
	}
	
	public void setLastPassingProcessPointId(String lastPassingProcessPointId) {
		this.lastPassingProcessPointId = lastPassingProcessPointId;
	}
	
	public String getDescription() {
		return StringUtils.trim(this.description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getKickoutStatus() {
		return this.kickoutStatus;
	}

	public void setKickoutStatus(int kickoutStatus) {
		this.kickoutStatus = kickoutStatus;
	}

	public String getComment() {
		return StringUtils.trim(this.comment);
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getKickoutUser() {
		return StringUtils.trim(this.kickoutUser);
	}
	
	public void setKickoutUser(String kickoutUser) {
		this.kickoutUser = kickoutUser;
	}
	
	public String getReleaseUser() {
		return StringUtils.trim(this.releaseUser);
	}
	
	public void setReleaseUser(String releaseUser) {
		this.releaseUser = releaseUser;
	}
	
	public String getReleaseComment() {
		return StringUtils.trim(this.releaseComment);
	}
	
	public void setReleaseComment(String releaseComment) {
		this.releaseComment = releaseComment;
	}

	public String getApproverName() {
		return StringUtils.trim(this.approverName);
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}
	
	public String getProductType() {
		return StringUtils.trim(this.productType);
	}
	
	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getDivisionId() {
		return StringUtils.trim(this.divisionId);
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	public String getLineId() {
		return StringUtils.trim(this.lineId);
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getProcessPointId() {
		return StringUtils.trim(this.processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getDivisionName() {
		return StringUtils.trim(this.divisionName);
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getLineName() {
		return StringUtils.trim(this.LineName);
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public String getProcessPointName() {
		return StringUtils.trim(this.processPointName);
	}

	public void setProcessPointName(String processPointName) {
		this.processPointName = processPointName;
	}

	public Timestamp getCreateTimestamp() {
		return this.createTimestamp;
	}

	public void setCreateTimestamp(Timestamp createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	
	public String getLastPassingProcessPointName() {
		return StringUtils.trim(this.lastPassingProcessPointName);
	}
	
	public void setLastPassingProcessPointName(String lastPassingProcessPointName) {
		this.lastPassingProcessPointName = lastPassingProcessPointName;
	}
	
	public String getKickoutStatusName() {
		return StringUtils.trim(this.kickoutStatusName);
	}
	
	public void setKickoutStatusName(String kickoutStatusName) {
		this.kickoutStatusName = kickoutStatusName;
	}
	
	public int getDivisionSequence() {
		return this.divisionSequence;
	}
	
	public void setDivisionSequence(int divisionSequence) {
		this.divisionSequence = divisionSequence;
	}
	
	public int getLineSequence() {
		return this.lineSequence;
	}
	
	public void setLineSequence(int lineSequence) {
		this.lineSequence = lineSequence;
	}
	
	public int getProcessPointSequence() {
		return this.processPointSequence;
	}
	
	public void setProcessPointSequence(int processPointSequence) {
		this.processPointSequence = processPointSequence;
	}
	
	public void setKickoutLocation(KickoutLocation kickoutLocation) {
		this.divisionId = kickoutLocation.getDivisionId();
		this.lineId = kickoutLocation.getLineId();
		this.processPointId = kickoutLocation.getLineId();
	}
	
	public void setKickout(Kickout kickout) {
		this.kickoutId = kickout.getKickoutId();
		this.productId = kickout.getProductId();
		this.productType = kickout.getProductType();
		this.description = kickout.getDescription();
		this.kickoutStatus = kickout.getKickoutStatus();
		this.comment = kickout.getComment();
		this.approverName = kickout.getApproverName();
		this.releaseComment = kickout.getReleaseComment();
		this.kickoutUser = kickout.getKickoutUser();
		this.releaseUser = kickout.getReleaseUser();
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lineId == null) ? 0 : lineId.hashCode());
		result = prime * result + ((LineName == null) ? 0 : LineName.hashCode());
		result = prime * result + ((approverName == null) ? 0 : approverName.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((createTimestamp == null) ? 0 : createTimestamp.hashCode());
		result = prime * result + ((dcSerialNumber == null) ? 0 : dcSerialNumber.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((divisionId == null) ? 0 : divisionId.hashCode());
		result = prime * result + ((divisionName == null) ? 0 : divisionName.hashCode());
		result = prime * result + ((dunnage == null) ? 0 : dunnage.hashCode());
		result = prime * result + (int) (kickoutId ^ (kickoutId >>> 32));
		result = prime * result + kickoutStatus;
		result = prime * result + ((kickoutStatusName == null) ? 0 : kickoutStatusName.hashCode());
		result = prime * result + ((lastPassingProcessPointId == null) ? 0 : lastPassingProcessPointId.hashCode());
		result = prime * result + ((lastPassingProcessPointName == null) ? 0 : lastPassingProcessPointName.hashCode());
		result = prime * result + ((mcSerialNumber == null) ? 0 : mcSerialNumber.hashCode());
		result = prime * result + ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result + ((processPointName == null) ? 0 : processPointName.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + ((productType == null) ? 0 : productType.hashCode());
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
		KickoutDto other = (KickoutDto) obj;
		if (lineId == null) {
			if (other.lineId != null)
				return false;
		} else if (!lineId.equals(other.lineId))
			return false;
		if (LineName == null) {
			if (other.LineName != null)
				return false;
		} else if (!LineName.equals(other.LineName))
			return false;
		if (approverName == null) {
			if (other.approverName != null)
				return false;
		} else if (!approverName.equals(other.approverName))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (createTimestamp == null) {
			if (other.createTimestamp != null)
				return false;
		} else if (!createTimestamp.equals(other.createTimestamp))
			return false;
		if (dcSerialNumber == null) {
			if (other.dcSerialNumber != null)
				return false;
		} else if (!dcSerialNumber.equals(other.dcSerialNumber))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (divisionId == null) {
			if (other.divisionId != null)
				return false;
		} else if (!divisionId.equals(other.divisionId))
			return false;
		if (divisionName == null) {
			if (other.divisionName != null)
				return false;
		} else if (!divisionName.equals(other.divisionName))
			return false;
		if (dunnage == null) {
			if (other.dunnage != null)
				return false;
		} else if (!dunnage.equals(other.dunnage))
			return false;
		if (kickoutId != other.kickoutId)
			return false;
		if (kickoutStatus != other.kickoutStatus)
			return false;
		if (kickoutStatusName == null) {
			if (other.kickoutStatusName != null)
				return false;
		} else if (!kickoutStatusName.equals(other.kickoutStatusName))
			return false;
		if (lastPassingProcessPointId == null) {
			if (other.lastPassingProcessPointId != null)
				return false;
		} else if (!lastPassingProcessPointId.equals(other.lastPassingProcessPointId))
			return false;
		if (lastPassingProcessPointName == null) {
			if (other.lastPassingProcessPointName != null)
				return false;
		} else if (!lastPassingProcessPointName.equals(other.lastPassingProcessPointName))
			return false;
		if (mcSerialNumber == null) {
			if (other.mcSerialNumber != null)
				return false;
		} else if (!mcSerialNumber.equals(other.mcSerialNumber))
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		if (processPointName == null) {
			if (other.processPointName != null)
				return false;
		} else if (!processPointName.equals(other.processPointName))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (productSpecCode == null) {
			if (other.productSpecCode != null)
				return false;
		} else if (!productSpecCode.equals(other.productSpecCode))
			return false;
		if (productType == null) {
			if (other.productType != null)
				return false;
		} else if (!productType.equals(other.productType))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "KickoutDto [kickoutId=" + kickoutId + ", productId=" + productId + ", mcSerialNumber=" + mcSerialNumber
				+ ", dcSerialNumber=" + dcSerialNumber + ", productSpecCode=" + productSpecCode + ", dunnage=" + dunnage
				+ ", lastPassingProcessPointId=" + lastPassingProcessPointId + ", description=" + description
				+ ", kickoutStatus=" + kickoutStatus + ", comment=" + comment + ", approverName=" + approverName
				+ ", productType=" + productType + ", divisionId=" + divisionId + ", lineId=" + lineId
				+ ", processPointId=" + processPointId + ", divisionName=" + divisionName + ", LineName=" + LineName
				+ ", processPointName=" + processPointName + ", createTimestamp=" + createTimestamp
				+ ", lastPassingProcessPointName=" + lastPassingProcessPointName + ", kickoutStatusName="
				+ kickoutStatusName + "]";
	}
}
