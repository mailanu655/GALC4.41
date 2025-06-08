package com.honda.galc.oif.dto;

import com.honda.galc.util.OutputData;

/**
 * 
 * <h3>BomDTO.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> BomDTO.java description </p>
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
public class BomExportDTO implements IOutputFormat{
	
	@OutputData(value="PART_NO")
	private String partNo;

	@OutputData(value="PLANT_LOC_CODE")
	private String plantLocCode;

	@OutputData(value="PART_COLOR_CODE")
	private String partColorCode;

	@OutputData(value="SUPPLIER_NO")
	private String supplierNo;

	@OutputData(value="PART_BLOCK_CODE")
	private String partBlockCode;

	@OutputData(value="PART_SECTION_CODE")
	private String partSectionCode;

	@OutputData(value="PART_ITEM_NO")
	private String partItemNo;

	@OutputData(value="TGT_SHIP_TO_CODE")
	private String tgtShipToCode;

	@OutputData(value="MTC_MODEL")
	private String mtcModel;

	@OutputData(value="MTC_COLOR")
	private String mtcColor;

	@OutputData(value="MTC_OPTION")
	private String mtcOption;

	@OutputData(value="MTC_TYPE")
	private String mtcType;

	@OutputData(value="INT_COLOR_CODE")
	private String intColorCode;

	@OutputData(value="EFF_BEG_DATE")
	private String effBegDate;
	
	@OutputData(value="TARGET_MFG_DEST_NO")
	private String targetMfgDestNo;

	@OutputData(value="EFF_END_DATE")
	private String effEndDate;

	@OutputData(value="PART_PROD_CODE")
	private String partProdCode;

	@OutputData(value="PROCLOC_GP_NO")
	private String proclocGpNo;

	@OutputData(value="PROCLOC_GP_SEQ_NO")
	private String proclocGpSeqNo;

	@OutputData(value="TGT_PLANT_LOC_CODE")
	private String tgtPlantLocCode;

	@OutputData(value="TGT_MODEL_DEV_CODE")
	private String tgtModelDevCode;

	@OutputData(value="PART_COLOR_ID_CODE")
	private String partColorIdCode;

	@OutputData(value="SUPPLIER_CAT_CODE")
	private String supplierCatCode;

	@OutputData(value="PART_QTY")
	private String partQty;

	@OutputData(value="DATA_UPD_DESC_TEXT")
	private String dataUpdDescText;

	@OutputData(value="TIMESTAMP_DATE")
	private String timestampDate;

	@OutputData(value="DC_FAM_CLASS_CODE")
	private String dcFamClassCode;

	@OutputData(value="DC_PART_NO")
	private String dcPartNo;

	@OutputData(value="DC_PART_NAME")
	private String dcPartName;

	@OutputData(value="PDDA_FIF_TYPE")
	private String pddaFifType;

	public BomExportDTO() {
	}

	
	
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

	public String getEffBegDate() {
		return effBegDate;
	}

	public void setEffBegDate(String effBegDate) {
		this.effBegDate = effBegDate;
	}

	public String getTargetMfgDestNo() {
		return targetMfgDestNo;
	}

	public void setTargetMfgDestNo(String targetMfgDestNo) {
		this.targetMfgDestNo = targetMfgDestNo;
	}

	public String getEffEndDate() {
		return effEndDate;
	}

	public void setEffEndDate(String effEndDate) {
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

	public String getPartQty() {
		return partQty;
	}

	public void setPartQty(String partQty) {
		this.partQty = partQty;
	}

	public String getDataUpdDescText() {
		return dataUpdDescText;
	}

	public void setDataUpdDescText(String dataUpdDescText) {
		this.dataUpdDescText = dataUpdDescText;
	}

	public String getTimestampDate() {
		return timestampDate;
	}

	public void setTimestampDate(String timestampDate) {
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

}
