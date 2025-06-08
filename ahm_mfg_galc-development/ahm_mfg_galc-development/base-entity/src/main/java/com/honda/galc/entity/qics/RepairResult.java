package com.honda.galc.entity.qics;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>RepairResult Class description</h3>
 * <p> RepairResult description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Jun 3, 2012
 *
 *
 */
@Entity
@Table(name="GAL222TBX")
public class RepairResult extends AuditEntry {
	@EmbeddedId
	private RepairResultId id;

	@Column(name="REPAIR_ASSOCIATE_NO")
	private String repairAssociateNo;

	@Column(name="ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;

	@Column(name="ACTUAL_PROBLEM_NAME")
	private String actualProblemName;

	@Column(name="REPAIR_METHOD_NAME")
	private String repairMethodName;

	@Column(name="REPAIR_TIME")
	private int repairTime;

	@Column(name="REPAIR_DEPT")
	private String repairDept;

	@Column(name="REPAIR_PROCESS_POINT_ID")
	private String repairProcessPointId;
	
	@Column(name="NAQ_REPAIR_ID")
	private long naqRepairId;

	private String comment;

	private static final long serialVersionUID = 1L;

	public RepairResult() {
		super();
	}

	public RepairResultId getId() {
		return this.id;
	}

	public void setId(RepairResultId id) {
		this.id = id;
	}

	public String getRepairAssociateNo() {
		return StringUtils.trim(this.repairAssociateNo);
	}

	public void setRepairAssociateNo(String repairAssociateNo) {
		this.repairAssociateNo = repairAssociateNo;
	}

	public Timestamp getActualTimestamp() {
		return this.actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	public String getActualProblemName() {
		return this.actualProblemName;
	}

	public void setActualProblemName(String actualProblemName) {
		this.actualProblemName = actualProblemName;
	}

	public String getRepairMethodName() {
		return StringUtils.trim(this.repairMethodName);
	}

	public void setRepairMethodName(String repairMethodName) {
		this.repairMethodName = repairMethodName;
	}

	public int getRepairTime() {
		return this.repairTime;
	}

	public void setRepairTime(int repairTime) {
		this.repairTime = repairTime;
	}

	public String getRepairDept() {
		return StringUtils.trim(this.repairDept);
	}

	public void setRepairDept(String repairDept) {
		this.repairDept = repairDept;
	}

	public String getRepairProcessPointId() {
		return StringUtils.trim(this.repairProcessPointId);
	}

	public void setRepairProcessPointId(String repairProcessPointId) {
		this.repairProcessPointId = repairProcessPointId;
	}

	public String getComment() {
		return StringUtils.trim(this.comment);
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public long getNaqRepairId() {
		return naqRepairId;
	}

	public void setNaqRepairId(long naqRepairId) {
		this.naqRepairId = naqRepairId;
	}
	
	public String getProductId() {
		return getId() != null ? getId().getProductId() : null;
	}
	
	public Integer getRepairId() {
		return getId() != null ? getId().getRepairId() : null;
	}
	
	public Integer getDefectresultid() {
		return getId() != null ? getId().getDefectresultid() : null;
	}
}
