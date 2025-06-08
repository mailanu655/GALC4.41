package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.ShippedStatus;

/**
 * 
 * <h3>EngineManifest</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EngineManifest description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Mar 17, 2017</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Mar 17, 2017
 */
@Entity
@Table(name="ENGINE_MANIFEST_TBX")
public class EngineManifest extends AuditEntry {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private EngineManifestId id;

	private String company ="";

	@Column(name="ENGINE_BASIC_PART")
	private String engineBasicPart = "";

	@Column(name="ENGINE_CASE_NO")
	private String engineCaseNo = "";

	@Column(name="ENGINE_EC_NO")
	private String engineEcNo = "";

	@Column(name="ENGINE_FIRED_IND")
	private String engineFiredInd ="";

	@Column(name="ENGINE_KD_LOT")
	private String engineKdLot = "";

	@Column(name="ENGINE_MODEL")
	private String engineModel ="";

	@Column(name="ENGINE_OPTION")
	private String engineOption ="";

	@Column(name="ENGINE_PC_NO")
	private String enginePcNo = "";

	@Column(name="ENGINE_SOURCE")
	private String engineSource = "";

	@Column(name="ENGINE_TYPE")
	private String engineType ="";

	@Column(name="MISSION_NO")
	private String missionNo = "";

	@Column(name="MODEL_YEAR_CODE")
	private String modelYearCode ="";

	@Column(name="UNSHIP_STATUS")
	private int unshipStatusId;


	private String vin = "";
	
	public EngineManifest() {
	}

	public EngineManifestId getId() {
		if(id == null)
			id = new EngineManifestId();
		return this.id;
	}

	public void setId(EngineManifestId id) {
		this.id = id;
	}

	public String getCompany() {
		return StringUtils.trim(this.company);
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getEngineBasicPart() {
		return StringUtils.trim(this.engineBasicPart);
	}

	public void setEngineBasicPart(String engineBasicPart) {
		this.engineBasicPart = engineBasicPart;
	}

	public String getEngineCaseNo() {
		return StringUtils.trim(this.engineCaseNo);
	}

	public void setEngineCaseNo(String engineCaseNo) {
		this.engineCaseNo = engineCaseNo;
	}

	public String getEngineEcNo() {
		return StringUtils.trim(this.engineEcNo);
	}

	public void setEngineEcNo(String engineEcNo) {
		this.engineEcNo = engineEcNo;
	}

	public String getEngineFiredInd() {
		return StringUtils.trim(this.engineFiredInd);
	}

	public void setEngineFiredInd(String engineFiredInd) {
		this.engineFiredInd = engineFiredInd;
	}

	public String getEngineKdLot() {
		return StringUtils.trim(this.engineKdLot);
	}

	public void setEngineKdLot(String engineKdLot) {
		this.engineKdLot = engineKdLot;
	}

	public String getEngineModel() {
		return StringUtils.trim(this.engineModel);
	}

	public void setEngineModel(String engineModel) {
		this.engineModel = engineModel;
	}

	public String getEngineOption() {
		return StringUtils.trim(this.engineOption);
	}

	public void setEngineOption(String engineOption) {
		this.engineOption = engineOption;
	}

	public String getEnginePcNo() {
		return StringUtils.trim(this.enginePcNo);
	}

	public void setEnginePcNo(String enginePcNo) {
		this.enginePcNo = enginePcNo;
	}

	public String getEngineSource() {
		return StringUtils.trim(this.engineSource);
	}

	public void setEngineSource(String engineSource) {
		this.engineSource = engineSource;
	}

	public String getEngineType() {
		return StringUtils.trim(this.engineType);
	}

	public void setEngineType(String engineType) {
		this.engineType = engineType;
	}

	public String getMissionNo() {
		return StringUtils.trim(this.missionNo);
	}

	public void setMissionNo(String missionNo) {
		if(missionNo != null)
			this.missionNo = missionNo;
	}

	public String getModelYearCode() {
		return StringUtils.trim(this.modelYearCode);
	}

	public void setModelYearCode(String modelYearCode) {
		this.modelYearCode = modelYearCode;
	}

	public int getUnshipStatusId() {
		return this.unshipStatusId;
	}

	public void setUnshipStatusId(int unshipStatusId) {
		this.unshipStatusId = unshipStatusId;
	}

	public ShippedStatus getUnshipStatus() {
		return ShippedStatus.getStatus(this.unshipStatusId);
	}

	public void setUnshipStatus(ShippedStatus status) {
		this.unshipStatusId = status.getId();
	}
	
	public String getVin() {
		return StringUtils.trim(this.vin);
	}

	public void setVin(String vin) {
		this.vin = vin;
	}


	@Override
	public String toString() {
		return "EngineManifest [id=" + id + ", company=" + company
				+ ", engineBasicPart=" + engineBasicPart + ", engineCaseNo="
				+ engineCaseNo + ", engineEcNo=" + engineEcNo
				+ ", engineFiredInd=" + engineFiredInd + ", engineKdLot="
				+ engineKdLot + ", engineModel=" + engineModel
				+ ", engineOption=" + engineOption + ", enginePcNo="
				+ enginePcNo + ", engineSource=" + engineSource
				+ ", engineType=" + engineType + ", missionNo=" + missionNo
				+ ", modelYearCode=" + modelYearCode + ", unshipStatusId="
				+ unshipStatusId + ", vin=" + vin + "]";
	}

	
	
}