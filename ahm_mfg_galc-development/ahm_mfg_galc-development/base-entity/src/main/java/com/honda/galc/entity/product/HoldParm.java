package com.honda.galc.entity.product;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>HoldParm</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> HoldParm description </p>
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
 * <TD>Apr 26, 2012</TD>
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
 * @since Apr 26, 2012
 */

@Entity
@Table(name="HOLD_PARM_TBX")
public class HoldParm extends AuditEntry {

	@Id
	@Column(name="HOLD_ID")
	private long holdId;

	private String department;
	
	@Column(name="MODEL_CODE")
	private String modelCode;

	@Column(name="MACHINE_NUMBER")
	private String machineNumber;

	@Column(name="DIE_NUMBER")
	private String dieNumber;
	
	@Column(name="CORE_NUMBER")
	private String coreNumber;

	@Column(name="START_DATE")
	private Date startDate;

	@Column(name="STOP_DATE")
	private Date stopDate;

	@Column(name="HOLD_REASON")
	private String holdReason;

	@Column(name="HOLD_ASSOCIATE_ID")
	private String holdAssociateId;

	@Column(name="HOLD_ASSOCIATE_NAME")
	private String holdAssociateName;

	@Column(name="ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;

	@Column(name="RELEASE_FLAG")
	private int releaseFlag;

	@Column(name="RELEASE_ASSOCIATE_ID")
	private String releaseAssociateId;

	@Column(name="RELEASE_ASSOCIATE_NAME")
	private String releaseAssociateName;

	@Column(name="RELEASE_TIMESTAMP")
	private Timestamp releaseTimestamp;

	@Column(name="QSR_ID")
	private int qsrId;

	private static final long serialVersionUID = 1L;

	public HoldParm() {
		super();
	}

	public long getHoldId() {
		return this.holdId;
	}

	public void setHoldId(long holdId) {
		this.holdId = holdId;
	}

	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getModelCode() {
		return StringUtils.trim(modelCode);
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}	
	
	public String getMachineNumber() {
		return this.machineNumber;
	}

	public void setMachineNumber(String machineNumber) {
		this.machineNumber = machineNumber;
	}

	public String getDieNumber() {
		return this.dieNumber;
	}

	public void setDieNumber(String dieNumber) {
		this.dieNumber = dieNumber;
	}

	public String getCoreNumber() {
		return StringUtils.trim(coreNumber);
	}

	public void setCoreNumber(String coreNumber) {
		this.coreNumber = coreNumber;
	}	
	
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStopDate() {
		return this.stopDate;
	}

	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}

	public String getHoldReason() {
		return this.holdReason;
	}

	public void setHoldReason(String holdReason) {
		this.holdReason = holdReason;
	}

	public String getHoldAssociateId() {
		return this.holdAssociateId;
	}

	public void setHoldAssociateId(String holdAssociateId) {
		this.holdAssociateId = holdAssociateId;
	}

	public String getHoldAssociateName() {
		return this.holdAssociateName;
	}

	public void setHoldAssociateName(String holdAssociateName) {
		this.holdAssociateName = holdAssociateName;
	}

	public Timestamp getActualTimestamp() {
		return this.actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	public int getReleaseFlag() {
		return this.releaseFlag;
	}

	public void setReleaseFlag(short releaseFlag) {
		this.releaseFlag = releaseFlag;
	}

	public String getReleaseAssociateId() {
		return this.releaseAssociateId;
	}

	public void setReleaseAssociateId(String releaseAssociateId) {
		this.releaseAssociateId = releaseAssociateId;
	}

	public String getReleaseAssociateName() {
		return this.releaseAssociateName;
	}

	public void setReleaseAssociateName(String releaseAssociateName) {
		this.releaseAssociateName = releaseAssociateName;
	}

	public Timestamp getReleaseTimestamp() {
		return this.releaseTimestamp;
	}

	public void setReleaseTimestamp(Timestamp releaseTimestamp) {
		this.releaseTimestamp = releaseTimestamp;
	}

	public int getQsrId() {
		return this.qsrId;
	}

	public void setQsrId(int qsrId) {
		this.qsrId = qsrId;
	}

	public Object getId() {
		return holdId;
	}
	
	@Override
	protected String toString(Object... objects) {
		return super.toString(holdId, department, machineNumber, dieNumber, qsrId);
	}
}
