package com.honda.galc.oif.dto;

import java.sql.Date;
import java.sql.Timestamp;

import com.honda.galc.entity.fif.Bom;
import com.honda.galc.entity.fif.BomId;
import com.honda.galc.util.GPCSData;

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
public class BomDTO {
	
	@GPCSData("PART_NO")
	private String partNo;

	@GPCSData("PLANT_LOC_CODE")
	private String plantLocCode;

	@GPCSData("PART_COLOR_CODE")
	private String partColorCode;

	@GPCSData("SUPPLIER_NO")
	private String supplierNo;

	@GPCSData("PART_BLOCK_CODE")
	private String partBlockCode;

	@GPCSData("PART_SECTION_CODE")
	private String partSectionCode;

	@GPCSData("PART_ITEM_NO")
	private String partItemNo;

	@GPCSData("TGT_SHIP_TO_CODE")
	private String tgtShipToCode;

	@GPCSData("MTC_MODEL")
	private String mtcModel;

	@GPCSData("MTC_COLOR")
	private String mtcColor;

	@GPCSData("MTC_OPTION")
	private String mtcOption;

	@GPCSData("MTC_TYPE")
	private String mtcType;

	@GPCSData("INT_COLOR_CODE")
	private String intColorCode;

	@GPCSData("EFF_BEG_DATE")
	private Date effBegDate;
	
	@GPCSData("TARGET_MFG_DEST_NO")
	private String targetMfgDestNo;

	@GPCSData("EFF_END_DATE")
	private Date effEndDate;

	@GPCSData("PART_PROD_CODE")
	private String partProdCode;

	@GPCSData("PROCLOC_GP_NO")
	private String proclocGpNo;

	@GPCSData("PROCLOC_GP_SEQ_NO")
	private String proclocGpSeqNo;

	@GPCSData("TGT_PLANT_LOC_CODE")
	private String tgtPlantLocCode;

	@GPCSData("TGT_MODEL_DEV_CODE")
	private String tgtModelDevCode;

	@GPCSData("PART_COLOR_ID_CODE")
	private String partColorIdCode;

	@GPCSData("SUPPLIER_CAT_CODE")
	private String supplierCatCode;

	@GPCSData("PART_QTY")
	private int partQty;

	@GPCSData("DATA_UPD_DESC_TEXT")
	private String dataUpdDescText;

	@GPCSData("TIMESTAMP_DATE")
	private Timestamp timestampDate;

	@GPCSData("DC_FAM_CLASS_CODE")
	private String dcFamClassCode;

	@GPCSData("DC_PART_NO")
	private String dcPartNo;

	@GPCSData("DC_PART_NAME")
	private String dcPartName;

	@GPCSData("PDDA_FIF_TYPE")
	private String pddaFifType;

	public BomDTO() {
	}

	public Bom deriveBom(){
		Bom bom = new Bom();
		bom.setId(deriveID());
		bom.setDataUpdDescText(dataUpdDescText);
		bom.setDcFamClassCode(dcFamClassCode);
		bom.setDcPartNo(dcPartNo);
		bom.setDcPartName(dcPartName);
		bom.setEffEndDate(effEndDate);
		bom.setPartColorIdCode(partColorIdCode);
		bom.setPartProdCode(partProdCode);
		bom.setPartQty(partQty);
		bom.setPddaFifType(pddaFifType);
		bom.setProclocGpNo(proclocGpNo);
		bom.setProclocGpSeqNo(proclocGpSeqNo);
		bom.setSupplierCatCode(supplierCatCode);
		bom.setTargetMfgDestNo(targetMfgDestNo);
		bom.setTgtModelDevCode(tgtModelDevCode);
		bom.setTgtPlantLocCode(tgtPlantLocCode);
		bom.setTimestampDate(timestampDate);
		return bom;
	}
	private BomId deriveID(){
		BomId bomId = new BomId();
		bomId.setEffBegDate(effBegDate);
		bomId.setIntColorCode(intColorCode);
		bomId.setMtcColor(mtcColor);
		bomId.setMtcModel(mtcModel);
		bomId.setMtcOption(mtcOption);
		bomId.setMtcType(mtcType);
		bomId.setPartBlockCode(partBlockCode);
		bomId.setPartColorCode(partColorCode);
		bomId.setPartItemNo(partItemNo);
		bomId.setPartNo(partNo);
		bomId.setPartSectionCode(partSectionCode);
		bomId.setPlantLocCode(plantLocCode);
		bomId.setSupplierNo(supplierNo);
		bomId.setTgtShipToCode(tgtShipToCode);
		return bomId;
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

}
