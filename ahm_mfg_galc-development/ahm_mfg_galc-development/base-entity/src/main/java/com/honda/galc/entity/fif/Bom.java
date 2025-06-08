package com.honda.galc.entity.fif;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name = "BOM_TBX")
public class Bom extends AuditEntry {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private BomId id;

	@Column(name = "TARGET_MFG_DEST_NO")
	private String targetMfgDestNo;

	@Column(name = "EFF_END_DATE")
	private Date effEndDate;

	@Column(name = "PART_PROD_CODE")
	private String partProdCode;

	@Column(name = "PROCLOC_GP_NO")
	private String proclocGpNo;

	@Column(name = "PROCLOC_GP_SEQ_NO")
	private String proclocGpSeqNo;

	@Column(name = "TGT_PLANT_LOC_CODE")
	private String tgtPlantLocCode;

	@Column(name = "TGT_MODEL_DEV_CODE")
	private String tgtModelDevCode;

	@Column(name = "PART_COLOR_ID_CODE")
	private String partColorIdCode;

	@Column(name = "SUPPLIER_CAT_CODE")
	private String supplierCatCode;

	@Column(name = "PART_QTY")
	private int partQty;

	@Column(name = "DATA_UPD_DESC_TEXT")
	private String dataUpdDescText;

	@Column(name = "TIMESTAMP_DATE")
	private Timestamp timestampDate;

	@Column(name = "DC_FAM_CLASS_CODE")
	private String dcFamClassCode;

	@Column(name = "DC_PART_NO")
	private String dcPartNo;

	@Column(name = "DC_PART_NAME")
	private String dcPartName;

	@Column(name = "PDDA_FIF_TYPE")
	private String pddaFifType;
	
	@Transient
	private boolean apply;
	
	@Transient
	private String modelYear;

	public Bom() {
		super();
	}

	public Bom(BomId id) {
		super();
		this.id = id;
	}

	public BomId getId() {
		return id;
	}

	public void setId(BomId id) {
		this.id = id;
	}

	public String getTargetMfgDestNo() {
		return StringUtils.trim(targetMfgDestNo);
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
		return StringUtils.trim(partProdCode);
	}

	public void setPartProdCode(String partProdCode) {
		this.partProdCode = partProdCode;
	}

	public String getProclocGpNo() {
		return StringUtils.trim(proclocGpNo);
	}

	public void setProclocGpNo(String proclocGpNo) {
		this.proclocGpNo = proclocGpNo;
	}

	public String getProclocGpSeqNo() {
		return StringUtils.trim(proclocGpSeqNo);
	}

	public void setProclocGpSeqNo(String proclocGpSeqNo) {
		this.proclocGpSeqNo = proclocGpSeqNo;
	}

	public String getTgtPlantLocCode() {
		return StringUtils.trim(tgtPlantLocCode);
	}

	public void setTgtPlantLocCode(String tgtPlantLocCode) {
		this.tgtPlantLocCode = tgtPlantLocCode;
	}

	public String getTgtModelDevCode() {
		return StringUtils.trim(tgtModelDevCode);
	}

	public void setTgtModelDevCode(String tgtModelDevCode) {
		this.tgtModelDevCode = tgtModelDevCode;
	}

	public String getPartColorIdCode() {
		return StringUtils.trim(partColorIdCode);
	}

	public void setPartColorIdCode(String partColorIdCode) {
		this.partColorIdCode = partColorIdCode;
	}

	public String getSupplierCatCode() {
		return StringUtils.trim(supplierCatCode);
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
		return StringUtils.trim(dataUpdDescText);
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
		return StringUtils.trim(dcFamClassCode);
	}

	public void setDcFamClassCode(String dcFamClassCode) {
		this.dcFamClassCode = dcFamClassCode;
	}

	public String getDcPartNo() {
		return StringUtils.trim(dcPartNo);
	}

	public void setDcPartNo(String dcPartNo) {
		this.dcPartNo = dcPartNo;
	}

	public String getDcPartName() {
		return StringUtils.trim(dcPartName);
	}

	public void setDcPartName(String dcPartName) {
		this.dcPartName = dcPartName;
	}

	public String getPddaFifType() {
		return StringUtils.trim(pddaFifType);
	}

	public void setPddaFifType(String pddaFifType) {
		this.pddaFifType = pddaFifType;
	}

	public boolean isApply() {
		return apply;
	}

	public void setApply(boolean apply) {
		this.apply = apply;
	}

	public String getModelYear() {
		if(StringUtils.isEmpty(this.modelYear)) return id.getMtcModel().substring(0, 1);
		return modelYear;
	}

	public void setModelYear(String modelYear) {
		this.modelYear = modelYear;
	}

	@Override
	public String toString() {
		return toString(id.getPartNo(), id.getPlantLocCode(),
				id.getPartColorCode(), id.getSupplierNo(),
				id.getPartBlockCode(), id.getPartSectionCode(),
				id.getPartItemNo(), id.getTgtShipToCode(), id.getMtcModel(),
				id.getMtcColor(), id.getMtcOption(), id.getMtcType(),
				id.getIntColorCode(), id.getEffBegDate(), getTargetMfgDestNo(),
				getEffEndDate(), getPartProdCode(), getProclocGpNo(),
				getProclocGpSeqNo(), getTgtPlantLocCode(),
				getTgtModelDevCode(), getPartColorIdCode(),
				getSupplierCatCode(), getPartQty(), getDataUpdDescText(),
				getTimestampDate(), getDcFamClassCode(), getDcPartNo(),
				getDcPartName(), getPddaFifType());
	}
	
	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((targetMfgDestNo == null) ? 0 : targetMfgDestNo.hashCode());
		result = prime * result
				+ ((effEndDate == null) ? 0 : effEndDate.hashCode());
		result = prime * result
				+ ((partProdCode == null) ? 0 : partProdCode.hashCode());
		result = prime * result
				+ ((proclocGpNo == null) ? 0 : proclocGpNo.hashCode());
		result = prime * result
				+ ((proclocGpSeqNo == null) ? 0 : proclocGpSeqNo.hashCode());
		result = prime * result
				+ ((tgtPlantLocCode == null) ? 0 : tgtPlantLocCode.hashCode());
		result = prime * result
				+ ((tgtModelDevCode == null) ? 0 : tgtModelDevCode.hashCode());
		result = prime * result
				+ ((partColorIdCode == null) ? 0 : partColorIdCode.hashCode());
		result = prime * result
				+ ((supplierCatCode == null) ? 0 : supplierCatCode.hashCode());
		result = prime * result
				+ ((dataUpdDescText == null) ? 0 : dataUpdDescText.hashCode());
		result = prime * result + partQty;
		result = prime * result
				+ ((timestampDate == null) ? 0 : timestampDate.hashCode());
		result = prime * result
				+ ((dcFamClassCode == null) ? 0 : dcFamClassCode.hashCode());
		result = prime * result
				+ ((dcPartNo == null) ? 0 : dcPartNo.hashCode());
		result = prime * result
				+ ((dcPartName == null) ? 0 : dcPartName.hashCode());
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
		Bom other = (Bom) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
		if (partQty != other.partQty) {
			return false;
		}
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
}
