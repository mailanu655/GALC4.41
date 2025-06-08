package com.honda.galc.dto.oif;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;
/**
 * 
 * <h3>IPPTagDTO.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> class to hold the IPP Info data </p>
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
 * <TD>KG</TD>
 * <TD>Dec 31, 2014</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author KG
 * @created Dec 31, 2014
 */
public class IPPTagDTO implements IDto {
	
	private static final long serialVersionUID = 1L;
	
	@DtoTag(outputName ="PLANT_CODE")
	private String plantCode;
	@DtoTag(outputName ="LINE_NUMBER")
	private String lineNumber;
	@DtoTag(outputName ="SCANNED")
	private String scanned;
	@DtoTag(outputName ="PSN_NUMBER")
	private String psnNumber;
	@DtoTag(outputName ="IPP_TAG_NUMBER")
	private String ippTagNumber;
	@DtoTag(outputName ="BUSINESS_DATE")
	private String businessDate;
	@DtoTag(outputName ="PROD_ORDER_LOT_NUMBER")
	private String prodOrderLotNumber;
	@DtoTag(outputName ="EOS_LOT_NUMBER")
	private String eosLotNumber;
	@DtoTag(outputName ="MTC_MODEL")
	private String mtcModel;
	@DtoTag(outputName ="MTC_TYPE")
	private String mtcType;
	@DtoTag(outputName ="MTC_OPTION")
	private String mtcOption;
	@DtoTag(outputName ="MTC_COLOR")
	private String mtcColor;
	@DtoTag(outputName ="MTC_INT_COLOR")
	private String mtcIntColor;
	@DtoTag(outputName ="EIN_NUMBER")
	private String einNumber;
	@DtoTag(outputName ="ASSY_SEQ_NO")
	private String assySeqNo;
	@DtoTag(outputName ="PROCESS_TSTP")
	private String processTstp;
	@DtoTag(outputName ="PSN_NO_CALC")
	private String psnNoCalc;
	@DtoTag(outputName ="LOT_NUMBER")
	private String lotNumber;
	@DtoTag(outputName ="USER_ID")
	private String userID;
	@DtoTag(outputName ="CREATE_DTTS")
	private String createDtts;
	@DtoTag(outputName ="IPP_TYPE")
	private String ippType;
	@DtoTag(outputName ="FILLER")
	private String filler;
	
	public IPPTagDTO() {
		super();
	}

	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public String getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getScanned() {
		return scanned;
	}

	public void setScanned(String scanned) {
		this.scanned = scanned;
	}

	public String getPsnNumber() {
		return psnNumber;
	}

	public void setPsnNumber(String psnNumber) {
		this.psnNumber = psnNumber;
	}

	public String getIppTagNumber() {
		return ippTagNumber;
	}

	public void setIppTagNumber(String ippTagNumber) {
		this.ippTagNumber = ippTagNumber;
	}

	public String getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(String businessDate) {
		this.businessDate = businessDate;
	}

	public String getProdOrderLotNumber() {
		return prodOrderLotNumber;
	}

	public void setProdOrderLotNumber(String prodOrderLotNumber) {
		this.prodOrderLotNumber = prodOrderLotNumber;
	}

	public String getEosLotNumber() {
		return eosLotNumber;
	}

	public void setEosLotNumber(String eosLotNumber) {
		this.eosLotNumber = eosLotNumber;
	}

	public String getMtcModel() {
		return mtcModel;
	}

	public void setMtcModel(String mtcModel) {
		this.mtcModel = mtcModel;
	}

	public String getMtcType() {
		return mtcType;
	}

	public void setMtcType(String mtcType) {
		this.mtcType = mtcType;
	}

	public String getMtcOption() {
		return mtcOption;
	}

	public void setMtcOption(String mtcOption) {
		this.mtcOption = mtcOption;
	}

	public String getMtcColor() {
		return mtcColor;
	}

	public void setMtcColor(String mtcColor) {
		this.mtcColor = mtcColor;
	}

	public String getMtcIntColor() {
		return mtcIntColor;
	}

	public void setMtcIntColor(String mtcIntColor) {
		this.mtcIntColor = mtcIntColor;
	}

	public String getEinNumber() {
		return einNumber;
	}

	public void setEinNumber(String einNumber) {
		this.einNumber = einNumber;
	}

	public String getAssySeqNo() {
		return assySeqNo;
	}

	public void setAssySeqNo(String assySeqNo) {
		this.assySeqNo = assySeqNo;
	}

	public String getProcessTstp() {
		return processTstp;
	}

	public void setProcessTstp(String processTstp) {
		this.processTstp = processTstp;
	}

	public String getPsnNoCalc() {
		return psnNoCalc;
	}

	public void setPsnNoCalc(String psnNoCalc) {
		this.psnNoCalc = psnNoCalc;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getCreateDtts() {
		return createDtts;
	}

	public void setCreateDtts(String createDtts) {
		this.createDtts = createDtts;
	}

	public String getIppType() {
		return ippType;
	}

	public void setIppType(String ippType) {
		this.ippType = ippType;
	}

	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
	}

	
}
