package com.honda.galc.oif.dto;

import com.honda.galc.util.OutputData;
/**
 * 
 * <h3>IPPInfoDTO.java</h3>
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
public class IPPInfoDTO implements IOutputFormat {
	@OutputData(value="PLANT_CODE")
	private String plantCode;
	@OutputData(value="LINE_NUMBER")
	private String lineNumber;
	@OutputData(value="SCANNED")
	private String scanned;
	@OutputData(value="PSN_NUMBER")
	private String psnNumber;
	@OutputData(value="IPP_TAG_NUMBER")
	private String ippTagNumber;
	@OutputData(value="BUSINESS_DATE")
	private String businessDate;
	@OutputData(value="PROD_ORDER_LOT_NUMBER")
	private String prodOrderLotNumber;
	@OutputData(value="EOS_LOT_NUMBER")
	private String eosLotNumber;
	@OutputData(value="MTC_MODEL")
	private String mtcModel;
	@OutputData(value="MTC_TYPE")
	private String mtcType;
	@OutputData(value="MTC_OPTION")
	private String mtcOption;
	@OutputData(value="MTC_COLOR")
	private String mtcColor;
	@OutputData(value="MTC_INT_COLOR")
	private String mtcIntColor;
	@OutputData(value="EIN_NUMBER")
	private String einNumber;
	@OutputData(value="ASSY_SEQ_NO")
	private String assySeqNo;
	@OutputData(value="PROCESS_TSTP")
	private String processTSTP;
	@OutputData(value="PSN_NO_CALC")
	private String psnNoCalc;
	@OutputData(value="LOT_NUMBER")
	private String lotNumber;
	@OutputData(value="USER_ID")
	private String userID;
	@OutputData(value="CREATE_DT_TS")
	private String createDTTS;
	@OutputData(value="IPP_TYPE")
	private String ippType;
	@OutputData(value="FILLER")
	private String filler;
	
	public IPPInfoDTO() {
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

	public String getProcessTSTP() {
		return processTSTP;
	}

	public void setProcessTSTP(String processTSTP) {
		this.processTSTP = processTSTP;
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

	public String getCreateDTTS() {
		return createDTTS;
	}

	public void setCreateDTTS(String createDTTS) {
		this.createDTTS = createDTTS;
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
