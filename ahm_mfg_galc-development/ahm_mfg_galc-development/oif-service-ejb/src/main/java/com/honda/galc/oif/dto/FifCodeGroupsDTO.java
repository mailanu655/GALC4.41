package com.honda.galc.oif.dto;

import java.util.Date;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.entity.fif.FifCodeGroups;
import com.honda.galc.entity.fif.FifCodeGroupsId;
import com.honda.galc.util.GPCSData;

/**
 * 
 * <h3>FifCodeGroupsDTO.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> FifCodeGroupsDTO.java description </p>
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

public class FifCodeGroupsDTO {
	@GPCSData("PLANT_CD")
	private String plantCd;

	@GPCSData("MODEL_YEAR")
	private String modelYear;

	@GPCSData("MODEL_CD")
	private String modelCd;

	@GPCSData("DEV_SEQ_CD")
	private String devSeqCd;

	@GPCSData("BASE_TYPE")
	private String baseType;

	@GPCSData("FIF_TYPE")
	private String fifType;

	@GPCSData("GROUP_CD")
	private String groupCd;

	@GPCSData("EFCT_BEG_DT")
	private Date efctBegDt;

	@GPCSData("GROUP_DESC")
	private String groupDesc;

	@GPCSData("EFCT_END_DT")
	private Date efctEndDt;

	@GPCSData("REQUIRED")
	private String required;

	@GPCSData("FIF_LENGTH")
	private int fifLength;

	@GPCSData("FIF_OFFSET")
	private int fifOffset;

	public FifCodeGroupsDTO() {
	}

	public FifCodeGroups deriveFifCodeGroups() {
		FifCodeGroups fifCodeGroups = new FifCodeGroups();
		fifCodeGroups.setId(deriveID());
		fifCodeGroups.setEfctEndDt(efctEndDt);
		fifCodeGroups.setFifLength(fifLength);
		fifCodeGroups.setFifOffset(fifOffset);
		fifCodeGroups.setGroupDesc(groupDesc);
		fifCodeGroups.setRequired(required);
		return fifCodeGroups;
	}

	private FifCodeGroupsId deriveID() {
		FifCodeGroupsId fifCodeGroupsId = new FifCodeGroupsId();
		fifCodeGroupsId.setBaseType(baseType);
		fifCodeGroupsId.setDevSeqCd(devSeqCd);
		fifCodeGroupsId.setEfctBegDt(efctBegDt);
		fifCodeGroupsId.setFifType(fifType);
		fifCodeGroupsId.setGroupCd(groupCd);
		fifCodeGroupsId.setModelCd(modelCd);
		fifCodeGroupsId.setModelYear(modelYear);
		fifCodeGroupsId.setPlantCd(plantCd);
		return fifCodeGroupsId;
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

	public String getBaseType() {
		return StringUtils.trim(baseType);
	}

	public void setBaseType(String baseType) {
		this.baseType = baseType;
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

	public String getGroupDesc() {
		return StringUtils.trim(groupDesc);
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	public Date getEfctEndDt() {
		return efctEndDt;
	}

	public void setEfctEndDt(Date efctEndDt) {
		this.efctEndDt = efctEndDt;
	}

	public String getRequired() {
		return StringUtils.trim(required);
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public int getFifLength() {
		return fifLength;
	}

	public void setFifLength(int fifLength) {
		this.fifLength = fifLength;
	}

	public int getFifOffset() {
		return fifOffset;
	}

	public void setFifOffset(int fifOffset) {
		this.fifOffset = fifOffset;
	}

}
