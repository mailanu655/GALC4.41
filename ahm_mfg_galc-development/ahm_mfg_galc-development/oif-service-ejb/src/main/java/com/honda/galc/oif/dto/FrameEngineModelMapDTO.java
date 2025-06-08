package com.honda.galc.oif.dto;

import java.sql.Timestamp;

import com.honda.galc.entity.product.FrameEngineModelMap;
import com.honda.galc.entity.product.FrameEngineModelMapId;
import com.honda.galc.util.GPCSData;

/*
* GMM117 Definition
*  Frm_Year_Code      |K|C| 0| 1|U| 
*  Frm_Model_Code     |K|C| 1| 3|U|
*  Frm_Type_Code      |K|C| 4| 3|U|
*  Frm_Option_Code    |K|C| 7| 3|U|
*  Eng_Year_Code      |K|C|10| 1|U|
*  Eng_Model_Code     |K|C|11| 3|U|
*  Eng_Type_Code      |K|C|14| 3|U|
*  Eng_Option_Code    |K|C|17| 3|U|
*  Updt_Userid        | |C|20|11|U|
*  Updt_Timestamp     | |T|31|26|U|
*  Filler             | |C|57|23|U|
*  
*  length is 80 char
*/

public class FrameEngineModelMapDTO { 
	
	@GPCSData("FRM_YEAR_CODE")
	private String frmYearCode;

	@GPCSData("FRM_MODEL_CODE")
	private String frmModelCode;

	@GPCSData("FRM_TYPE_CODE")
	private String frmTypeCode;

	@GPCSData("FRM_OPTION_CODE")
	private String frmOptionCode;

	@GPCSData("ENG_YEAR_CODE")
	private String engYearCode;

	@GPCSData("ENG_MODEL_CODE")
	private String engModelCode;

	@GPCSData("ENG_TYPE_CODE")
	private String engTypeCode;

	@GPCSData("ENG_OPTION_CODE")
	private String engOptionCode;

	@GPCSData("UPDT_USERID")
	private String updtUserid;

	@GPCSData("UPDT_TIMESTAMP")
	private Timestamp updtTimestamp;

	public FrameEngineModelMapDTO() {
	}
	
	public String getFrmYearCode() {
		return this.frmYearCode;
	}

	public void setFrmYearCode(String frmYearCode) {
		this.frmYearCode = frmYearCode;
	}

	public String getFrmModelCode() {
		return this.frmModelCode;
	}

	public void setFrmModelCode(String frmModelCode) {
		this.frmModelCode = frmModelCode;
	}

	public String getFrmTypeCode() {
		return this.frmTypeCode;
	}

	public void setFrmTypeCode(String frmTypeCode) {
		this.frmTypeCode = frmTypeCode;
	}

	public String getFrmOptionCode() {
		return this.frmOptionCode;
	}

	public void setFrmOptionCode(String frmOptionCode) {
		this.frmOptionCode = frmOptionCode;
	}

	public String getEngYearCode() {
		return this.engYearCode;
	}

	public void setEngYearCode(String engYearCode) {
		this.engYearCode = engYearCode;
	}

	public String getEngModelCode() {
		return this.engModelCode;
	}

	public void setEngModelCode(String engModelCode) {
		this.engModelCode = engModelCode;
	}

	public String getEngTypeCode() {
		return this.engTypeCode;
	}

	public void setEngTypeCode(String engTypeCode) {
		this.engTypeCode = engTypeCode;
	}

	public String getEngOptionCode() {
		return this.engOptionCode;
	}

	public void setEngOptionCode(String engOptionCode) {
		this.engOptionCode = engOptionCode;
	}

	public String getUpdtUserid() {
		return updtUserid;
	}

	public void setUpdtUserid(String updtUserid) {
		this.updtUserid = updtUserid;
	}

	public Timestamp getUpdtTimestamp() {
		return this.updtTimestamp;
	}

	// Copy Constructor
	public FrameEngineModelMapDTO(FrameEngineModelMapDTO frameEngineModelMapDTO) {
		this.frmYearCode = frameEngineModelMapDTO.getFrmYearCode();
		this.frmModelCode = frameEngineModelMapDTO.getFrmModelCode();
		this.frmTypeCode = frameEngineModelMapDTO.getFrmTypeCode();
		this.frmOptionCode = frameEngineModelMapDTO.getFrmOptionCode();
		this.engYearCode = frameEngineModelMapDTO.getEngYearCode();
		this.engModelCode = frameEngineModelMapDTO.getEngModelCode();
		this.engTypeCode = frameEngineModelMapDTO.getEngTypeCode();
		this.engOptionCode = frameEngineModelMapDTO.getEngOptionCode();
		this.updtUserid = frameEngineModelMapDTO.getUpdtUserid();
		this.updtTimestamp = frameEngineModelMapDTO.getUpdtTimestamp();	
	}

	public FrameEngineModelMap deriveFrameEngineModelMap(){
		FrameEngineModelMap frameEngineModelMap = new FrameEngineModelMap();
		frameEngineModelMap.setId(this.deriveID());
		frameEngineModelMap.setUpdtUserId(this.updtUserid);
		frameEngineModelMap.setUpdateTimestamp(this.updtTimestamp);
		return frameEngineModelMap;
	}
	
	private FrameEngineModelMapId deriveID() {
		FrameEngineModelMapId frameEngineModelMapId = new FrameEngineModelMapId();
		frameEngineModelMapId.setFrmModelYearCode(this.frmYearCode);
		frameEngineModelMapId.setFrmModelCode(this.frmModelCode);
		frameEngineModelMapId.setFrmModelTypeCode(this.frmTypeCode);
		frameEngineModelMapId.setFrmModelOptionCode(this.frmOptionCode);
		frameEngineModelMapId.setEngModelYearCode(this.engYearCode);
		frameEngineModelMapId.setEngModelCode(this.engModelCode);
		frameEngineModelMapId.setEngModelTypeCode(this.engTypeCode);
		frameEngineModelMapId.setEngModelOptionCode(this.engOptionCode);
		return frameEngineModelMapId;
	}
}


