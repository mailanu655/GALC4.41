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
@Table(name="GAL712TBX")
public class LetProgramResultHistory extends AuditEntry {
	
	@EmbeddedId
	private LetProgramResultHistoryId id;

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


	private static final long serialVersionUID = 1L;

	public LetProgramResultHistory() {
		super();
	}

	public void setId(LetProgramResultHistoryId id) {
		this.id = id;
	}

	public LetProgramResultHistoryId getId() {
		
		return id;
	}

	public String getInspectionPgmStatus() {
		return StringUtils.trimToEmpty(inspectionPgmStatus);
	}

	public void setInspectionPgmStatus(String inspectionPgmStatus) {
		this.inspectionPgmStatus = inspectionPgmStatus;
	}

	public String getProcessStep() {
		return StringUtils.trimToEmpty(processStep);
	}

	public void setProcessStep(String processStep) {
		this.processStep = processStep;
	}

	public String getProcessStatus() {
		return StringUtils.trimToEmpty(processStatus);
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public Timestamp getProcessStartTimestamp() {
		return processStartTimestamp;
	}

	public void setProcessStartTimestamp(Timestamp processStartTimestamp) {
		this.processStartTimestamp = processStartTimestamp;
	}

	public Timestamp getProcessEndTimestamp() {
		return processEndTimestamp;
	}

	public void setProcessEndTimestamp(Timestamp processEndTimestamp) {
		this.processEndTimestamp = processEndTimestamp;
	}

	public String getLetTerminalId() {
		return StringUtils.trimToEmpty(letTerminalId);
	}

	public void setLetTerminalId(String letTerminalId) {
		this.letTerminalId = letTerminalId;
	}

	public String getLetResultCal() {
		return StringUtils.trimToEmpty(letResultCal);
	}

	public void setLetResultCal(String letResultCal) {
		this.letResultCal = letResultCal;
	}

	public String getLetResultDcrev() {
		return StringUtils.trimToEmpty(letResultDcrev);
	}

	public void setLetResultDcrev(String letResultDcrev) {
		this.letResultDcrev = letResultDcrev;
	}

	public String getSoftwareVersion() {
		return StringUtils.trimToEmpty(softwareVersion);
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public String getLetOperatorId() {
		return StringUtils.trimToEmpty(letOperatorId);
	}

	public void setLetOperatorId(String letOperatorId) {
		this.letOperatorId = letOperatorId;
	}

	public int getInCycleRetestNum() {
		return inCycleRetestNum;
	}

	public void setInCycleRetestNum(int inCycleRetestNum) {
		this.inCycleRetestNum = inCycleRetestNum;
	}
	
	@Override
	public String toString() {
		return toString(getId().getProductId(),getId().getTestSeq(),getId().getHistorySeq(),getId().getInspectionPgmId());
}
   

}
