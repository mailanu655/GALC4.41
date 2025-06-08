package com.honda.galc.service.msip.dto.inbound;

import java.sql.Date;
import java.sql.Timestamp;

import com.honda.galc.util.ToStringUtil;

/**
 * 
 * <h3>BomDTO.java</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * BomDTO.java description
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
 * <TR>
 * <TD>Jiamei Li</TD>
 * <TD>Feb 25, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD>
 * </TR>
 * </TABLE>
 */
public class PmcbomDto implements IMsipInboundDto {
	private static final long serialVersionUID = 1L;
	private String partNo;
	private String plantLocCode;
	private String partColorCode;
	private String supplierNo;
	private String partBlockCode;
	private String partSectionCode;
	private String partItemNo;
	private String tgtShipToCode;
	private String mtcModel;
	private String mtcColor;
	private String mtcOption;
	private String mtcType;
	private String intColorCode;
	private Date effBegDate;
	private String targetMfgDestNo;
	private Date effEndDate;
	private String partProdCode;
	private String proclocGpNo;
	private String proclocGpSeqNo;
	private String tgtPlantLocCode;
	private String tgtModelDevCode;
	private String partColorIdCode;
	private String supplierCatCode;
	private int partQty;
	private String dataUpdDescText;
	private Timestamp timestampDate;
	private String dcFamClassCode;
	private String dcPartNo;
	private String dcPartName;
	private String pddaFifType;

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public String getPlantLocCode() {
		return plantLocCode;
	}

	public void setPlantLocCode(String plantLocCode) {
		this.plantLocCode = plantLocCode;
	}

	public String getPartColorCode() {
		return partColorCode;
	}

	public void setPartColorCode(String partColorCode) {
		this.partColorCode = partColorCode;
	}

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	public String getPartBlockCode() {
		return partBlockCode;
	}

	public void setPartBlockCode(String partBlockCode) {
		this.partBlockCode = partBlockCode;
	}

	public String getPartSectionCode() {
		return partSectionCode;
	}

	public void setPartSectionCode(String partSectionCode) {
		this.partSectionCode = partSectionCode;
	}

	public String getPartItemNo() {
		return partItemNo;
	}

	public void setPartItemNo(String partItemNo) {
		this.partItemNo = partItemNo;
	}

	public String getTgtShipToCode() {
		return tgtShipToCode;
	}

	public void setTgtShipToCode(String tgtShipToCode) {
		this.tgtShipToCode = tgtShipToCode;
	}

	public String getMtcModel() {
		return mtcModel;
	}

	public void setMtcModel(String mtcModel) {
		this.mtcModel = mtcModel;
	}

	public String getMtcColor() {
		return mtcColor;
	}

	public void setMtcColor(String mtcColor) {
		this.mtcColor = mtcColor;
	}

	public String getMtcOption() {
		return mtcOption;
	}

	public void setMtcOption(String mtcOption) {
		this.mtcOption = mtcOption;
	}

	public String getMtcType() {
		return mtcType;
	}

	public void setMtcType(String mtcType) {
		this.mtcType = mtcType;
	}

	public String getIntColorCode() {
		return intColorCode;
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

	public String getTargetMfgDestNo() {
		return targetMfgDestNo;
	}

	public void setTargetMfgDestNo(String targetMfgDestNo) {
		this.targetMfgDestNo = targetMfgDestNo;
	}

	public Date getEffEndDate() {
		return effEndDate;
	}

	public void setEffEndDate(Date effEndDate) {
		this.effEndDate = effEndDate;
	}

	public String getPartProdCode() {
		return partProdCode;
	}

	public void setPartProdCode(String partProdCode) {
		this.partProdCode = partProdCode;
	}

	public String getProclocGpNo() {
		return proclocGpNo;
	}

	public void setProclocGpNo(String proclocGpNo) {
		this.proclocGpNo = proclocGpNo;
	}

	public String getProclocGpSeqNo() {
		return proclocGpSeqNo;
	}

	public void setProclocGpSeqNo(String proclocGpSeqNo) {
		this.proclocGpSeqNo = proclocGpSeqNo;
	}

	public String getTgtPlantLocCode() {
		return tgtPlantLocCode;
	}

	public void setTgtPlantLocCode(String tgtPlantLocCode) {
		this.tgtPlantLocCode = tgtPlantLocCode;
	}

	public String getTgtModelDevCode() {
		return tgtModelDevCode;
	}

	public void setTgtModelDevCode(String tgtModelDevCode) {
		this.tgtModelDevCode = tgtModelDevCode;
	}

	public String getPartColorIdCode() {
		return partColorIdCode;
	}

	public void setPartColorIdCode(String partColorIdCode) {
		this.partColorIdCode = partColorIdCode;
	}

	public String getSupplierCatCode() {
		return supplierCatCode;
	}

	public void setSupplierCatCode(String supplierCatCode) {
		this.supplierCatCode = supplierCatCode;
	}

	public int getPartQty() {
		return partQty;
	}

	public void setPartQty(int partQty) {
		this.partQty = partQty;
	}

	public String getDataUpdDescText() {
		return dataUpdDescText;
	}

	public void setDataUpdDescText(String dataUpdDescText) {
		this.dataUpdDescText = dataUpdDescText;
	}

	public Timestamp getTimestampDate() {
		return timestampDate;
	}

	public void setTimestampDate(Timestamp timestampDate) {
		this.timestampDate = timestampDate;
	}

	public String getDcFamClassCode() {
		return dcFamClassCode;
	}

	public void setDcFamClassCode(String dcFamClassCode) {
		this.dcFamClassCode = dcFamClassCode;
	}

	public String getDcPartNo() {
		return dcPartNo;
	}

	public void setDcPartNo(String dcPartNo) {
		this.dcPartNo = dcPartNo;
	}

	public String getDcPartName() {
		return dcPartName;
	}

	public void setDcPartName(String dcPartName) {
		this.dcPartName = dcPartName;
	}

	public String getPddaFifType() {
		return pddaFifType;
	}

	public void setPddaFifType(String pddaFifType) {
		this.pddaFifType = pddaFifType;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + partQty;
		result = prime * result + ((partNo == null) ? 0 : partNo.hashCode());
		result = prime * result + ((plantLocCode == null) ? 0 : plantLocCode.hashCode());
		result = prime * result + ((partColorCode == null) ? 0 : partColorCode.hashCode());
		result = prime * result + ((supplierNo == null) ? 0 : supplierNo.hashCode());
		result = prime * result + ((partBlockCode == null) ? 0 : partBlockCode.hashCode());
		result = prime * result + ((partSectionCode == null) ? 0 : partSectionCode.hashCode());
		result = prime * result + ((partItemNo == null) ? 0 : partItemNo.hashCode());
		result = prime * result + ((tgtShipToCode == null) ? 0 : tgtShipToCode.hashCode());
		result = prime * result + ((mtcModel == null) ? 0 : mtcModel.hashCode());
		result = prime * result + ((mtcColor == null) ? 0 : mtcColor.hashCode());
		result = prime * result + ((mtcOption == null) ? 0 : mtcOption.hashCode());
		result = prime * result + ((mtcType == null) ? 0 : mtcType.hashCode());
		result = prime * result + ((intColorCode == null) ? 0 : intColorCode.hashCode());
		result = prime * result + ((effBegDate == null) ? 0 : effBegDate.hashCode());
		result = prime * result + ((targetMfgDestNo == null) ? 0 : targetMfgDestNo.hashCode());
		result = prime * result + ((effEndDate == null) ? 0 : effEndDate.hashCode());
		result = prime * result + ((partProdCode == null) ? 0 : partProdCode.hashCode());
		result = prime * result + ((proclocGpNo == null) ? 0 : proclocGpNo.hashCode());
		result = prime * result + ((proclocGpSeqNo == null) ? 0 : proclocGpSeqNo.hashCode());
		result = prime * result + ((tgtPlantLocCode == null) ? 0 : tgtPlantLocCode.hashCode());
		result = prime * result + ((tgtModelDevCode == null) ? 0 : tgtModelDevCode.hashCode());
		result = prime * result + ((partColorIdCode == null) ? 0 : partColorIdCode.hashCode());
		result = prime * result + ((supplierCatCode == null) ? 0 : supplierCatCode.hashCode());
		result = prime * result + ((dataUpdDescText == null) ? 0 : dataUpdDescText.hashCode());
		result = prime * result + ((timestampDate == null) ? 0 : timestampDate.hashCode());
		result = prime * result + ((dcFamClassCode == null) ? 0 : dcFamClassCode.hashCode());
		result = prime * result + ((dcPartNo == null) ? 0 : dcPartNo.hashCode());
		result = prime * result + ((dcPartName == null) ? 0 : dcPartName.hashCode());
		result = prime * result + ((pddaFifType == null) ? 0 : pddaFifType.hashCode());
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
		PmcbomDto other = (PmcbomDto) obj;
		if (partQty != other.partQty) 
			return false;
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
		if (targetMfgDestNo == null) {
			if (other.targetMfgDestNo != null)
				return false;
		} else if (!targetMfgDestNo.equals(other.targetMfgDestNo))
			return false;
		if (effEndDate == null) {
			if (other.effEndDate != null)
				return false;
		} else if (!effEndDate.equals(other.effEndDate))
			return false;
		if (partProdCode == null) {
			if (other.partProdCode != null)
				return false;
		} else if (!partProdCode.equals(other.partProdCode))
			return false;
		if (proclocGpNo == null) {
			if (other.proclocGpNo != null)
				return false;
		} else if (!proclocGpNo.equals(other.proclocGpNo))
			return false;
		if (proclocGpSeqNo == null) {
			if (other.proclocGpSeqNo != null)
				return false;
		} else if (!proclocGpSeqNo.equals(other.proclocGpSeqNo))
			return false;
		if (tgtPlantLocCode == null) {
			if (other.tgtPlantLocCode != null)
				return false;
		} else if (!tgtPlantLocCode.equals(other.tgtPlantLocCode))
			return false;
		if (tgtModelDevCode == null) {
			if (other.tgtModelDevCode != null)
				return false;
		} else if (!tgtModelDevCode.equals(other.tgtModelDevCode))
			return false;
		if (partColorIdCode == null) {
			if (other.partColorIdCode != null)
				return false;
		} else if (!partColorIdCode.equals(other.partColorIdCode))
			return false;
		if (supplierCatCode == null) {
			if (other.supplierCatCode != null)
				return false;
		} else if (!supplierCatCode.equals(other.supplierCatCode))
			return false;
		if (dataUpdDescText == null) {
			if (other.dataUpdDescText != null)
				return false;
		} else if (!dataUpdDescText.equals(other.dataUpdDescText))
			return false;
		if (timestampDate == null) {
			if (other.timestampDate != null)
				return false;
		} else if (!timestampDate.equals(other.timestampDate))
			return false;
		if (dcFamClassCode == null) {
			if (other.dcFamClassCode != null)
				return false;
		} else if (!dcFamClassCode.equals(other.dcFamClassCode))
			return false;
		if (dcPartNo == null) {
			if (other.dcPartNo != null)
				return false;
		} else if (!dcPartNo.equals(other.dcPartNo))
			return false;
		if (dcPartName == null) {
			if (other.dcPartName != null)
				return false;
		} else if (!dcPartName.equals(other.dcPartName))
			return false;
		if (pddaFifType == null) {
			if (other.pddaFifType != null)
				return false;
		} else if (!pddaFifType.equals(other.pddaFifType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}
