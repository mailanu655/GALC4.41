package com.honda.galc.entity.pdda;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * Apr 09, 2014
 */
@Embeddable
public class UnitPartId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="MAINTENANCE_ID", nullable=false)
	private int maintenanceId;

	@Column(name="MTC_MODEL", nullable=false, length=3)
	private String mtcModel;

	@Column(name="MTC_TYPE", nullable=false, length=3)
	private String mtcType;

	@Column(name="MTC_OPTION", nullable=false, length=3)
	private String mtcOption;

	@Column(name="PART_ITEM_NO", nullable=false, length=8)
	private String partItemNo;

	@Column(name="PART_NO", nullable=false, length=18)
	private String partNo;

	@Column(name="PART_SECTION_CODE", nullable=false, length=3)
	private String partSectionCode;
	
	@Column(name="PART_QTY", nullable=false)
	private short partQty;
	
	@Column(name="PART_MARKING_NO", nullable=false, length=4)
	private String partMarkingNo;
	
	@Column(name="TGT_EFF_BEG_DATE", nullable=false)
	private Date tgtEffBegDate;
	
	@Column(name="PART_EFF_DATE", nullable=false)
	private Date partEffDate;
	
	@Column(name="DELIVERY_LOCATION", nullable=false, length=4)
	private String deliveryLocation;
	
	@Column(name="PART_NAME", nullable=false, length=50)
	private String partName;

    public UnitPartId() {}
    
	public int getMaintenanceId() {
		return this.maintenanceId;
	}
	
	public void setMaintenanceId(int maintenanceId) {
		this.maintenanceId = maintenanceId;
	}
	
	public String getMtcModel() {
		return StringUtils.trim(this.mtcModel);
	}
	
	public void setMtcModel(String mtcModel) {
		this.mtcModel = mtcModel;
	}
	
	public String getMtcType() {
		return StringUtils.stripEnd(this.mtcType, null);
	}
	
	public void setMtcType(String mtcType) {
		this.mtcType = mtcType;
	}
	
	public String getMtcOption() {
		return StringUtils.trim(this.mtcOption);
	}
	
	public void setMtcOption(String mtcOption) {
		this.mtcOption = mtcOption;
	}
	
	public String getPartItemNo() {
		return StringUtils.trim(this.partItemNo);
	}
	
	public void setPartItemNo(String partItemNo) {
		this.partItemNo = partItemNo;
	}
	
	public String getPartNo() {
		return StringUtils.trim(this.partNo);
	}
	
	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}
	
	public String getPartSectionCode() {
		return StringUtils.trim(this.partSectionCode);
	}
	
	public void setPartSectionCode(String partSectionCode) {
		this.partSectionCode = partSectionCode;
	}
	
	public Date getPartEffDate() {
		return this.partEffDate;
	}

	public void setPartEffDate(Date partEffDate) {
		this.partEffDate = partEffDate;
	}

	public String getPartMarkingNo() {
		return StringUtils.trim(this.partMarkingNo);
	}

	public void setPartMarkingNo(String partMarkingNo) {
		this.partMarkingNo = partMarkingNo;
	}

	public short getPartQty() {
		return this.partQty;
	}

	public void setPartQty(short partQty) {
		this.partQty = partQty;
	}
	
	public Date getTgtEffBegDate() {
		return this.tgtEffBegDate;
	}

	public void setTgtEffBegDate(Date tgtEffBegDate) {
		this.tgtEffBegDate = tgtEffBegDate;
	}
	
	public String getDeliveryLocation() {
		return StringUtils.trim(deliveryLocation);
	}

	public void setDeliveryLocation(String deliveryLocation) {
		this.deliveryLocation = deliveryLocation;
	}
	
	public String getPartName() {
		return StringUtils.trim(this.partName);
	}
	
	public void setPartName(String partName) {
		this.partName = partName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + maintenanceId;
		result = prime * result
				+ ((mtcModel == null) ? 0 : mtcModel.hashCode());
		result = prime * result
				+ ((mtcOption == null) ? 0 : mtcOption.hashCode());
		result = prime * result + ((mtcType == null) ? 0 : mtcType.hashCode());
		result = prime * result
				+ ((partItemNo == null) ? 0 : partItemNo.hashCode());
		result = prime * result + ((partNo == null) ? 0 : partNo.hashCode());
		result = prime * result
				+ ((partSectionCode == null) ? 0 : partSectionCode.hashCode());
		result = prime * result + partQty;
		result = prime * result
				+ ((partMarkingNo == null) ? 0 : partMarkingNo.hashCode());
		result = prime * result
				+ ((tgtEffBegDate == null) ? 0 : tgtEffBegDate.hashCode());
		result = prime * result
				+ ((partEffDate == null) ? 0 : partEffDate.hashCode());
		result = prime * result
				+ ((deliveryLocation == null) ? 0 : deliveryLocation.hashCode());
		result = prime * result
				+ ((partName == null) ? 0 : partName.hashCode());
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
		UnitPartId other = (UnitPartId) obj;
		if (maintenanceId != other.maintenanceId)
			return false;
		if (mtcModel == null) {
			if (other.mtcModel != null)
				return false;
		} else if (!mtcModel.equals(other.mtcModel))
			return false;
		if (mtcOption == null) {
			if (other.mtcOption != null)
				return false;
		} else if (!mtcOption.equals(other.mtcOption))
			return false;
		if (mtcType == null) {
			if (other.mtcType != null)
				return false;
		} else if (!mtcType.equals(other.mtcType))
			return false;
		if (partItemNo == null) {
			if (other.partItemNo != null)
				return false;
		} else if (!partItemNo.equals(other.partItemNo))
			return false;
		if (partNo == null) {
			if (other.partNo != null)
				return false;
		} else if (!partNo.equals(other.partNo))
			return false;
		if (partSectionCode == null) {
			if (other.partSectionCode != null)
				return false;
		} else if (!partSectionCode.equals(other.partSectionCode))
			return false;
		if (deliveryLocation == null) {
			if (other.deliveryLocation != null)
				return false;
		} else if (!deliveryLocation.equals(other.deliveryLocation))
			return false;
		if (partEffDate == null) {
			if (other.partEffDate != null)
				return false;
		} else if (!partEffDate.equals(other.partEffDate))
			return false;
		if (partMarkingNo == null) {
			if (other.partMarkingNo != null)
				return false;
		} else if (!partMarkingNo.equals(other.partMarkingNo))
			return false;
		if (partQty != other.partQty)
			return false;
		if (tgtEffBegDate == null) {
			if (other.tgtEffBegDate != null)
				return false;
		} else if (!tgtEffBegDate.equals(other.tgtEffBegDate))
			return false;
		if (partName == null) {
			if (other.partName != null)
				return false;
		} else if (!partName.equals(other.partName))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getMaintenanceId(), getMtcModel(), 
				getMtcType(), getMtcOption(), getPartItemNo(), getPartNo(), getPartSectionCode(), 
				getPartEffDate(), getPartMarkingNo(), getPartQty(), getTgtEffBegDate(), 
				getDeliveryLocation(), getPartName());
	}
}