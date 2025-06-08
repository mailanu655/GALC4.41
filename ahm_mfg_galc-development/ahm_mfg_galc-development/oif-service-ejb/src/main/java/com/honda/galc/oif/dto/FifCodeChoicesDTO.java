package com.honda.galc.oif.dto;

import java.util.Date;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.entity.fif.FifCodeChoices;
import com.honda.galc.entity.fif.FifCodeChoicesId;
import com.honda.galc.util.GPCSData;

/**
 * 
 * <h3>FifCodeChoicesDTO.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> FifCodeChoicesDTO.java description </p>
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
 * <TD>Xiaomei Ma</TD>
 * <TD>Feb 19, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 * </TABLE>
 */

public class FifCodeChoicesDTO {
	@GPCSData("PLANT_CD")
	private String plantCd;

	@GPCSData("MODEL_YEAR")
	private String modelYear;

	@GPCSData("MODEL_CD")
	private String modelCd;

	@GPCSData("DEV_SEQ_CD")
	private String devSeqCd;

	@GPCSData("FIF_CODE")
	private String fifCode;

	@GPCSData("FIF_TYPE")
	private String fifType;

	@GPCSData("GROUP_CD")
	private String groupCd;

	@GPCSData("EFCT_BEG_DT")
	private Date efctBegDt;
	
	@GPCSData("FIF_DESC")
	private String fifDesc;

	@GPCSData("EFCT_END_DT")
	private Date efctEndDt;
	
	public FifCodeChoicesDTO(){
		
	}

		public FifCodeChoices deriveFifCodeChoices() {
			FifCodeChoices fifCodeChoices = new FifCodeChoices();
			fifCodeChoices.setId(deriveID());
			fifCodeChoices.setEfctEndDt(efctEndDt);
			fifCodeChoices.setFifDesc(fifDesc);
			return fifCodeChoices;
		}

		private FifCodeChoicesId deriveID() {
			FifCodeChoicesId fifCodeChoicesId = new FifCodeChoicesId();
			fifCodeChoicesId.setDevSeqCd(devSeqCd);
			fifCodeChoicesId.setEfctBegDt(efctBegDt);
			fifCodeChoicesId.setFifCode(fifCode);
			fifCodeChoicesId.setFifType(fifType);
			fifCodeChoicesId.setGroupCd(groupCd);
			fifCodeChoicesId.setModelCd(modelCd);
			fifCodeChoicesId.setModelYear(modelYear);
			fifCodeChoicesId.setPlantCd(plantCd);
			return fifCodeChoicesId;
		}

		public String getPlantCd() {
			return StringUtils.trim(plantCd);
		}

		public void setPlantCd(String plantCd) {
			this.plantCd = plantCd;
		}

		public String getModelYear() {
			return StringUtils.trim(modelYear);
		}

		public void setModelYear(String modelYear) {
			this.modelYear = modelYear;
		}

		public String getModelCd() {
			return StringUtils.trim(modelCd);
		}

		public void setModelCd(String modelCd) {
			this.modelCd = modelCd;
		}

		public String getDevSeqCd() {
			return StringUtils.trim(devSeqCd);
		}

		public void setDevSeqCd(String devSeqCd) {
			this.devSeqCd = devSeqCd;
		}

		public String getFifCode() {
			return StringUtils.trim(fifCode);
		}

		public void setFifCode(String fifCode) {
			this.fifCode = fifCode;
		}

		public String getFifType() {
			return StringUtils.trim(fifType);
		}

		public void setFifType(String fifType) {
			this.fifType = fifType;
		}

		public String getGroupCd() {
			return StringUtils.trim(groupCd);
		}

		public void setGroupCd(String groupCd) {
			this.groupCd = groupCd;
		}

		public Date getEfctBegDt() {
			return efctBegDt;
		}

		public void setEfctBegDt(Date efctBegDt) {
			this.efctBegDt = efctBegDt;
		}

		public String getFifDesc() {
			return StringUtils.trim(fifDesc);
		}

		public void setFifDesc(String fifDesc) {
			this.fifDesc = fifDesc;
		}

		public Date getEfctEndDt() {
			return efctEndDt;
		}

		public void setEfctEndDt(Date efctEndDt) {
			this.efctEndDt = efctEndDt;
		}
}
