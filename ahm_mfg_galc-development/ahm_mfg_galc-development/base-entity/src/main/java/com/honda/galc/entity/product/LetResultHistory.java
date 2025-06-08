package com.honda.galc.entity.product;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
@Entity
@Table(name="GAL710TBX")
public class LetResultHistory extends AuditEntry {
	@EmbeddedId
	private LetResultHistoryId id;


	@Column(name="BUILD_CODE")
	private String buildCode;

	@Column(name="TEST_ID")
	private String testId;

	@Column(name="INSPECTION_MTO")
	private String inspectionMto;

	@Column(name="SEQ_STEP_FILE")
	private String seqStepFile;

	@Column(name="CONT_STEP_FILE")
	private String contStepFile;

	@Column(name="SEQ_RANGE")
	private String seqRange;

	@Column(name="TOTAL_RESULT_STATUS")
	private String totalResultStatus;

	@Column(name="START_TIMESTAMP")
	private Timestamp startTimestamp;

	@Column(name="END_TIMESTAMP")
	private Timestamp endTimestamp;

	@Column(name="LET_MFG_AREA_CODE")
	private String letMfgAreaCode;

	@Column(name="LET_MFG_NO")
	private String letMfgNo;

	@Column(name="LET_LINE_NO")
	private String letLineNo;


	@Column(name="BASE_RELEASE")
	private String baseRelease;

	@Column(name="PRODUCTION")
	private String production;

	@Column(name="ADDITIONAL_DATA")
	private String additionalData;

	private static final long serialVersionUID = 1L;

	public LetResultHistory() {
		super();
	}

	public void setId(LetResultHistoryId id) {
		this.id = id;
	}


	public String getBuildCode() {
		return StringUtils.trim(this.buildCode);
	}

	public void setBuildCode(String buildCode) {
		this.buildCode = buildCode;
	}

	public String getTestId() {
		return this.testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getInspectionMto() {
		return StringUtils.trim(this.inspectionMto);
	}

	public void setInspectionMto(String inspectionMto) {
		this.inspectionMto = inspectionMto;
	}

	public String getSeqStepFile() {
		return StringUtils.trim(this.seqStepFile);
	}

	public void setSeqStepFile(String seqStepFile) {
		this.seqStepFile = seqStepFile;
	}

	public String getContStepFile() {
		return StringUtils.trim(this.contStepFile);
	}

	public void setContStepFile(String contStepFile) {
		this.contStepFile = contStepFile;
	}

	public String getSeqRange() {
		return StringUtils.trim(this.seqRange);
	}

	public void setSeqRange(String seqRange) {
		this.seqRange = seqRange;
	}

	public String getTotalResultStatus() {
		return StringUtils.trim(this.totalResultStatus);
	}

	public void setTotalResultStatus(String totalResultStatus) {
		this.totalResultStatus = totalResultStatus;
	}

	public Timestamp getStartTimestamp() {
		return this.startTimestamp;
	}

	public void setStartTimestamp(Timestamp startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public Timestamp getEndTimestamp() {
		return this.endTimestamp;
	}

	public void setEndTimestamp(Timestamp endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	public String getLetMfgAreaCode() {
		return StringUtils.trim(this.letMfgAreaCode);
	}

	public void setLetMfgAreaCode(String letMfgAreaCode) {
		this.letMfgAreaCode = letMfgAreaCode;
	}

	public String getLetMfgNo() {
		return StringUtils.trim(this.letMfgNo);
	}

	public void setLetMfgNo(String letMfgNo) {
		this.letMfgNo = letMfgNo;
	}

	public String getLetLineNo() {
		return StringUtils.trim(this.letLineNo);
	}

	public void setLetLineNo(String letLineNo) {
		this.letLineNo = letLineNo;
	}

	public String getBaseRelease() {
		return StringUtils.trim(this.baseRelease);
	}

	public void setBaseRelease(String baseRelease) {
		this.baseRelease = baseRelease;
	}

	public String getProduction() {
		return StringUtils.trim(this.production);
	}

	public void setProduction(String production) {
		this.production = production;
	}

	public String getAdditionalData() {
		return StringUtils.trim(this.additionalData);
	}

	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}

	public LetResultHistoryId getId() {
		
		return id;
	}

	@Override
	public String toString() {
		return toString(getId().getProductId(),getId().getTestSeq(),getId().getHistorySeq());
}
}
