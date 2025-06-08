package com.honda.galc.entity.product;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@MappedSuperclass()
public abstract class LetProgramResult extends AuditEntry {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8586569599378400497L;

	@EmbeddedId
	private LetProgramResultId id;

	@Column(name="INSPECTION_PGM_STATUS")
	private String inspectionPgmStatus;

	@Column(name="PROCESS_STEP")
	private String processStep;

	@Column(name="PROCESS_STATUS")
	private String processStatus;

	@Column(name="PROCESS_START_TIMESTAMP")
	private Timestamp processStartTimestamp;

	@Column(name="PROCESS_END_TIMESTAMP")
	private Timestamp processEndTimestamp;

	@Column(name="LET_TERMINAL_ID")
	private String letTerminalId;

	@Column(name="LET_RESULT_CAL")
	private String letResultCal;

	@Column(name="LET_RESULT_DCREV")
	private String letResultDcrev;

	@Column(name="SOFTWARE_VERSION")
	private String softwareVersion;

	@Column(name="LET_OPERATOR_ID")
	private String letOperatorId;

	@Column(name="IN_CYCLE_RETEST_NUM")
	private int inCycleRetestNum;

	public LetProgramResultId getId() {
		return id;
	}

	public void setId(final LetProgramResultId id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + inCycleRetestNum;
		result = prime
				* result
				+ ((inspectionPgmStatus == null) ? 0 : inspectionPgmStatus
						.hashCode());
		result = prime * result
				+ ((letOperatorId == null) ? 0 : letOperatorId.hashCode());
		result = prime * result
				+ ((letResultCal == null) ? 0 : letResultCal.hashCode());
		result = prime * result
				+ ((letResultDcrev == null) ? 0 : letResultDcrev.hashCode());
		result = prime * result
				+ ((letTerminalId == null) ? 0 : letTerminalId.hashCode());
		result = prime
				* result
				+ ((processEndTimestamp == null) ? 0 : processEndTimestamp
						.hashCode());
		result = prime
				* result
				+ ((processStartTimestamp == null) ? 0 : processStartTimestamp
						.hashCode());
		result = prime * result
				+ ((processStatus == null) ? 0 : processStatus.hashCode());
		result = prime * result
				+ ((processStep == null) ? 0 : processStep.hashCode());
		result = prime * result
				+ ((softwareVersion == null) ? 0 : softwareVersion.hashCode());
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
		LetProgramResult other = (LetProgramResult) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (inCycleRetestNum != other.inCycleRetestNum)
			return false;
		if (inspectionPgmStatus == null) {
			if (other.inspectionPgmStatus != null)
				return false;
		} else if (!inspectionPgmStatus.equals(other.inspectionPgmStatus))
			return false;
		if (letOperatorId == null) {
			if (other.letOperatorId != null)
				return false;
		} else if (!letOperatorId.equals(other.letOperatorId))
			return false;
		if (letResultCal == null) {
			if (other.letResultCal != null)
				return false;
		} else if (!letResultCal.equals(other.letResultCal))
			return false;
		if (letResultDcrev == null) {
			if (other.letResultDcrev != null)
				return false;
		} else if (!letResultDcrev.equals(other.letResultDcrev))
			return false;
		if (letTerminalId == null) {
			if (other.letTerminalId != null)
				return false;
		} else if (!letTerminalId.equals(other.letTerminalId))
			return false;
		if (processEndTimestamp == null) {
			if (other.processEndTimestamp != null)
				return false;
		} else if (!processEndTimestamp.equals(other.processEndTimestamp))
			return false;
		if (processStartTimestamp == null) {
			if (other.processStartTimestamp != null)
				return false;
		} else if (!processStartTimestamp.equals(other.processStartTimestamp))
			return false;
		if (processStatus == null) {
			if (other.processStatus != null)
				return false;
		} else if (!processStatus.equals(other.processStatus))
			return false;
		if (processStep == null) {
			if (other.processStep != null)
				return false;
		} else if (!processStep.equals(other.processStep))
			return false;
		if (softwareVersion == null) {
			if (other.softwareVersion != null)
				return false;
		} else if (!softwareVersion.equals(other.softwareVersion))
			return false;
		return true;
	}

	public String getInspectionPgmStatus() {
		return StringUtils.trim(inspectionPgmStatus);
	}

	public void setInspectionPgmStatus(final String inspectionPgmStatus) {
		this.inspectionPgmStatus = inspectionPgmStatus;
	}

	public String getProcessStep() {
		return StringUtils.trim(processStep);
	}

	public void setProcessStep(final String processStep) {
		this.processStep = processStep;
	}

	public String getProcessStatus() {
		return StringUtils.trim(processStatus);
	}

	public void setProcessStatus(final String processStatus) {
		this.processStatus = processStatus;
	}

	public Timestamp getProcessStartTimestamp() {
		return processStartTimestamp;
	}

	public void setProcessStartTimestamp(final Timestamp processStartTimestamp) {
		this.processStartTimestamp = processStartTimestamp;
	}

	public Timestamp getProcessEndTimestamp() {
		return processEndTimestamp;
	}

	public void setProcessEndTimestamp(final Timestamp processEndTimestamp) {
		this.processEndTimestamp = processEndTimestamp;
	}

	public String getLetTerminalId() {
		return StringUtils.trim(letTerminalId);
	}

	public void setLetTerminalId(final String letTerminalId) {
		this.letTerminalId = letTerminalId;
	}

	public String getLetResultCal() {
		return StringUtils.trim(letResultCal);
	}

	public void setLetResultCal(final String letResultCal) {
		this.letResultCal = letResultCal;
	}

	public String getLetResultDcrev() {
		return StringUtils.trim(letResultDcrev);
	}

	public void setLetResultDcrev(final String letResultDcrev) {
		this.letResultDcrev = letResultDcrev;
	}

	public String getSoftwareVersion() {
		return StringUtils.trim(softwareVersion);
	}

	public void setSoftwareVersion(final String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public String getLetOperatorId() {
		return StringUtils.trim(letOperatorId);
	}

	public void setLetOperatorId(final String letOperatorId) {
		this.letOperatorId = letOperatorId;
	}

	public int getInCycleRetestNum() {
		return inCycleRetestNum;
	}

	public void setInCycleRetestNum(final int inCycleRetestNum) {
		this.inCycleRetestNum = inCycleRetestNum;
	}
	
	@Override
	public String toString() {
		return toString(getId().getProductId(), getId().getEndTimestamp(), getId().getTestSeq(), getId().getInspectionPgmId(), getInspectionPgmStatus(), getProcessStep(), getProcessStatus(), getProcessStartTimestamp(), getProcessEndTimestamp(),
				getLetTerminalId(), getLetResultCal(), getLetResultDcrev(), getSoftwareVersion(), getLetOperatorId(), getInCycleRetestNum());
	}
}
