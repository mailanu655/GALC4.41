package com.honda.galc.entity.product;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name="GAL701TBX")
public class LetResult extends AuditEntry {
	@EmbeddedId
	private LetResultId id;

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

	public LetResult() {
		super();
	}

	public LetResult(LetResultId newId) {
		super();
		setId(newId);
	}

	public LetResultId getId() {
		return this.id;
	}

	public void setId(LetResultId id) {
		this.id = id;
	}

	public String getBuildCode() {
		return StringUtils.trim(this.buildCode);
	}

	public void setBuildCode(String buildCode) {
		this.buildCode = buildCode;
	}

	public String getTestId() {
		return StringUtils.trim(this.testId);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((additionalData == null) ? 0 : additionalData.hashCode());
		result = prime * result
				+ ((baseRelease == null) ? 0 : baseRelease.hashCode());
		result = prime * result
				+ ((buildCode == null) ? 0 : buildCode.hashCode());
		result = prime * result
				+ ((contStepFile == null) ? 0 : contStepFile.hashCode());
		result = prime * result
				+ ((endTimestamp == null) ? 0 : endTimestamp.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((inspectionMto == null) ? 0 : inspectionMto.hashCode());
		result = prime * result
				+ ((letLineNo == null) ? 0 : letLineNo.hashCode());
		result = prime * result
				+ ((letMfgAreaCode == null) ? 0 : letMfgAreaCode.hashCode());
		result = prime * result
				+ ((letMfgNo == null) ? 0 : letMfgNo.hashCode());
		result = prime * result
				+ ((production == null) ? 0 : production.hashCode());
		result = prime * result
				+ ((seqRange == null) ? 0 : seqRange.hashCode());
		result = prime * result
				+ ((seqStepFile == null) ? 0 : seqStepFile.hashCode());
		result = prime * result
				+ ((startTimestamp == null) ? 0 : startTimestamp.hashCode());
		result = prime * result + ((testId == null) ? 0 : testId.hashCode());
		result = prime
				* result
				+ ((totalResultStatus == null) ? 0 : totalResultStatus
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		LetResult other = (LetResult) obj;
		if (additionalData == null) {
			if (other.additionalData != null)
				return false;
		} else if (!additionalData.equals(other.additionalData))
			return false;
		if (baseRelease == null) {
			if (other.baseRelease != null)
				return false;
		} else if (!baseRelease.equals(other.baseRelease))
			return false;
		if (buildCode == null) {
			if (other.buildCode != null)
				return false;
		} else if (!buildCode.equals(other.buildCode))
			return false;
		if (contStepFile == null) {
			if (other.contStepFile != null)
				return false;
		} else if (!contStepFile.equals(other.contStepFile))
			return false;
		if (endTimestamp == null) {
			if (other.endTimestamp != null)
				return false;
		} else if (!endTimestamp.equals(other.endTimestamp))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (inspectionMto == null) {
			if (other.inspectionMto != null)
				return false;
		} else if (!inspectionMto.equals(other.inspectionMto))
			return false;
		if (letLineNo == null) {
			if (other.letLineNo != null)
				return false;
		} else if (!letLineNo.equals(other.letLineNo))
			return false;
		if (letMfgAreaCode == null) {
			if (other.letMfgAreaCode != null)
				return false;
		} else if (!letMfgAreaCode.equals(other.letMfgAreaCode))
			return false;
		if (letMfgNo == null) {
			if (other.letMfgNo != null)
				return false;
		} else if (!letMfgNo.equals(other.letMfgNo))
			return false;
		if (production == null) {
			if (other.production != null)
				return false;
		} else if (!production.equals(other.production))
			return false;
		if (seqRange == null) {
			if (other.seqRange != null)
				return false;
		} else if (!seqRange.equals(other.seqRange))
			return false;
		if (seqStepFile == null) {
			if (other.seqStepFile != null)
				return false;
		} else if (!seqStepFile.equals(other.seqStepFile))
			return false;
		if (startTimestamp == null) {
			if (other.startTimestamp != null)
				return false;
		} else if (!startTimestamp.equals(other.startTimestamp))
			return false;
		if (testId == null) {
			if (other.testId != null)
				return false;
		} else if (!testId.equals(other.testId))
			return false;
		if (totalResultStatus == null) {
			if (other.totalResultStatus != null)
				return false;
		} else if (!totalResultStatus.equals(other.totalResultStatus))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return toString(getId().getProductId(),getId().getTestSeq());
	}

}
