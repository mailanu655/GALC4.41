package com.honda.galc.entity.fif;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class BomId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "PART_NO")
	private String partNo;

	@Column(name = "PLANT_LOC_CODE")
	private String plantLocCode;

	@Column(name = "PART_COLOR_CODE")
	private String partColorCode;

	@Column(name = "SUPPLIER_NO")
	private String supplierNo;

	@Column(name = "PART_BLOCK_CODE")
	private String partBlockCode;

	@Column(name = "PART_SECTION_CODE")
	private String partSectionCode;

	@Column(name = "PART_ITEM_NO")
	private String partItemNo;

	@Column(name = "TGT_SHIP_TO_CODE")
	private String tgtShipToCode;

	@Column(name = "MTC_MODEL")
	private String mtcModel;

	@Column(name = "MTC_COLOR")
	private String mtcColor;

	@Column(name = "MTC_OPTION")
	private String mtcOption;

	@Column(name = "MTC_TYPE")
	private String mtcType;

	@Column(name = "INT_COLOR_CODE")
	private String intColorCode;

	@Column(name = "EFF_BEG_DATE")
	private Date effBegDate;

	public BomId() {
		super();
	}

	public BomId(String partNo, String plantLocCode, String partColorCode,
			String supplierNo, String partBlockCode, String partSectionCode,
			String partItemNo, String tgtShipToCode, String mtcModel,
			String mtcColor, String mtcOption, String mtcType,
			String intColorCode, Date effBegDate) {
		super();
		this.partNo = partNo;
		this.plantLocCode = plantLocCode;
		this.partColorCode = partColorCode;
		this.supplierNo = supplierNo;
		this.partBlockCode = partBlockCode;
		this.partSectionCode = partSectionCode;
		this.partItemNo = partItemNo;
		this.tgtShipToCode = tgtShipToCode;
		this.mtcModel = mtcModel;
		this.mtcColor = mtcColor;
		this.mtcOption = mtcOption;
		this.mtcType = mtcType;
		this.intColorCode = intColorCode;
		this.effBegDate = effBegDate;
	}

	public String getPartNo() {
		return StringUtils.trim(partNo);
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public String getPlantLocCode() {
		return StringUtils.trim(plantLocCode);
	}

	public void setPlantLocCode(String plantLocCode) {
		this.plantLocCode = plantLocCode;
	}

	public String getPartColorCode() {
		return StringUtils.trim(partColorCode);
	}

	public void setPartColorCode(String partColorCode) {
		this.partColorCode = partColorCode;
	}

	public String getSupplierNo() {
		return StringUtils.trim(supplierNo);
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	public String getPartBlockCode() {
		return StringUtils.trim(partBlockCode);
	}

	public void setPartBlockCode(String partBlockCode) {
		this.partBlockCode = partBlockCode;
	}

	public String getPartSectionCode() {
		return StringUtils.trim(partSectionCode);
	}

	public void setPartSectionCode(String partSectionCode) {
		this.partSectionCode = partSectionCode;
	}

	public String getPartItemNo() {
		return StringUtils.trim(partItemNo);
	}

	public void setPartItemNo(String partItemNo) {
		this.partItemNo = partItemNo;
	}

	public String getTgtShipToCode() {
		return StringUtils.trim(tgtShipToCode);
	}

	public void setTgtShipToCode(String tgtShipToCode) {
		this.tgtShipToCode = tgtShipToCode;
	}

	public String getMtcModel() {
		return StringUtils.trim(mtcModel);
	}

	public void setMtcModel(String mtcModel) {
		this.mtcModel = mtcModel;
	}

	public String getMtcColor() {
		return StringUtils.trim(mtcColor);
	}

	public void setMtcColor(String mtcColor) {
		this.mtcColor = mtcColor;
	}

	public String getMtcOption() {
		return StringUtils.trim(mtcOption);
	}

	public void setMtcOption(String mtcOption) {
		this.mtcOption = mtcOption;
	}

	public String getMtcType() {
		return StringUtils.stripEnd(mtcType, null);
	}

	public void setMtcType(String mtcType) {
		this.mtcType = mtcType;
	}

	public String getIntColorCode() {
		return StringUtils.trim(intColorCode);
	}

	public void setIntColorCode(String intColorCode) {
		this.intColorCode = intColorCode;
	}

	public Date getEffBegDate() {
		return effBegDate;
	}

	public void setEffBegDate(Date effBegDate) {
		this.effBegDate = effBegDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((partNo == null) ? 0 : partNo.hashCode());
		result = prime * result
				+ ((plantLocCode == null) ? 0 : plantLocCode.hashCode());
		result = prime * result
				+ ((partColorCode == null) ? 0 : partColorCode.hashCode());
		result = prime * result
				+ ((supplierNo == null) ? 0 : supplierNo.hashCode());
		result = prime * result
				+ ((partBlockCode == null) ? 0 : partBlockCode.hashCode());
		result = prime * result
				+ ((partSectionCode == null) ? 0 : partSectionCode.hashCode());
		result = prime * result
				+ ((partItemNo == null) ? 0 : partItemNo.hashCode());
		result = prime * result
				+ ((tgtShipToCode == null) ? 0 : tgtShipToCode.hashCode());
		result = prime * result
				+ ((mtcModel == null) ? 0 : mtcModel.hashCode());
		result = prime * result
				+ ((mtcColor == null) ? 0 : mtcColor.hashCode());
		result = prime * result
				+ ((mtcOption == null) ? 0 : mtcOption.hashCode());
		result = prime * result + ((mtcType == null) ? 0 : mtcType.hashCode());
		result = prime * result
				+ ((intColorCode == null) ? 0 : intColorCode.hashCode());
		result = prime * result
				+ ((effBegDate == null) ? 0 : effBegDate.hashCode());
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
		BomId other = (BomId) obj;
		if (partNo == null) {
			if (other.partNo != null)
				return false;
		} else if (!partNo.equals(other.partNo))
			return false;
		if (plantLocCode == null) {
			if (other.plantLocCode != null)
				return false;
		} else if (!plantLocCode.equals(other.plantLocCode))
			return false;
		if (partColorCode == null) {
			if (other.partColorCode != null)
				return false;
		} else if (!partColorCode.equals(other.partColorCode))
			return false;
		if (supplierNo == null) {
			if (other.supplierNo != null)
				return false;
		} else if (!supplierNo.equals(other.supplierNo))
			return false;
		if (partBlockCode == null) {
			if (other.partBlockCode != null)
				return false;
		} else if (!partBlockCode.equals(other.partBlockCode))
			return false;
		if (partSectionCode == null) {
			if (other.partSectionCode != null)
				return false;
		} else if (!partSectionCode.equals(other.partSectionCode))
			return false;
		if (partItemNo == null) {
			if (other.partItemNo != null)
				return false;
		} else if (!partItemNo.equals(other.partItemNo))
			return false;
		if (tgtShipToCode == null) {
			if (other.tgtShipToCode != null)
				return false;
		} else if (!tgtShipToCode.equals(other.tgtShipToCode))
			return false;
		if (mtcModel == null) {
			if (other.mtcModel != null)
				return false;
		} else if (!mtcModel.equals(other.mtcModel))
			return false;
		if (mtcColor == null) {
			if (other.mtcColor != null)
				return false;
		} else if (!mtcColor.equals(other.mtcColor))
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
		if (intColorCode == null) {
			if (other.intColorCode != null)
				return false;
		} else if (!intColorCode.equals(other.intColorCode))
			return false;
		if (effBegDate == null) {
			if (other.effBegDate != null)
				return false;
		} else if (!effBegDate.equals(other.effBegDate))
			return false;
		return true;
	}
}
