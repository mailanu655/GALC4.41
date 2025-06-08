package com.honda.galc.entity.oif;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.OifRunStatus;

/**
 * Entity implementation class for Entity: RunHistory
 *
 */
@Entity
@Table(name = "OIF_RUN_HISTORY_TBX")
public class RunHistory extends AuditEntry implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="RUN_HISTORY_ID")
	private long runHistoryId;
	
	@Column(name="JOB_NAME")
	private String jobName;
	
	@Column(name="INTERFACE_ID")
	private String interfaceId;

	@Column(name = "START_TIMESTAMP")
	private Timestamp jobStartTimestamp;

	@Column(name = "END_TIMESTAMP")
	private Timestamp jobEndTimestamp;

	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private OifRunStatus status;
	
	@Column(name="USER_NAME")
	private String userName;
	
	@Column(name = "SUCCESS_COUNT")
	private Integer successCount = 0;
	
	@Column(name = "FAILED_COUNT")
	private Integer failedCount = 0;
	
	@Column(name = "INCOMING_FILE_NAME")
	private String incomingFileName;
	
	@Column(name = "LOCAL_FILE_NAME")
	private String localFileName;
	
	@Column(name = "OUTGOING_FILE_NAME")
	private String outgoingFileName;
	

	public long getRunHistoryId() {
		return runHistoryId;
	}

	public void setRunHistoryId(long runHistoryId) {
		this.runHistoryId = runHistoryId;
	}

	public Object getId() {
		return this.runHistoryId;
	}

	public String getIncomingFileName() {
		return StringUtils.trim(incomingFileName);
	}

	public void setIncomingFileName(String incomingFileName) {
		this.incomingFileName = incomingFileName;
	}

	public String getOutgoingFileName() {
		return StringUtils.trim(outgoingFileName);
	}

	public void setOutgoingFileName(String outgoingFileName) {
		this.outgoingFileName = outgoingFileName;
	}


	public String getJobName() {
		return StringUtils.trim(jobName);
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	
	public String getInterfaceId() {
		return StringUtils.trim(interfaceId);
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}

	public Timestamp getJobStartTimestamp() {
		return jobStartTimestamp;
	}

	public void setJobStartTimestamp(Timestamp jobStartTimestamp) {
		this.jobStartTimestamp = jobStartTimestamp;
	}

	public Timestamp getJobEndTimestamp() {
		return jobEndTimestamp;
	}

	public void setJobEndTimestamp(Timestamp jobEndTimestamp) {
		this.jobEndTimestamp = jobEndTimestamp;
	}

	
	public OifRunStatus getStatus() {
		return status;
	}

	public void setStatus(OifRunStatus status) {
		this.status = status;
	}

	public String getUserName() {
		return StringUtils.trim(userName);
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	public Integer getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}

	public Integer getFailedCount() {
		return failedCount;
	}

	public void setFailedCount(Integer failedCount) {
		this.failedCount = failedCount;
	}

	public String getLocalFileName() {
		return StringUtils.trim(localFileName);
	}

	public void setLocalFileName(String localFileName) {
		this.localFileName = localFileName;
	}

	
	
   
}
