package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>HoldReasonMapping Class description</h3>
 * <p>
 * HoldReasonMapping description
 * </p>
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
 * @author Prasanna Parvathaneni<br>
 *         Dec 1 2016
 * 
 * 
 */
@Entity
@Table(name = "HOLD_REASON_MAPPING_TBX")
public class HoldReasonMapping extends AuditEntry {

	@Id
	@Column(name = "REASON_MAPPING_ID")
	private int reasonMappingId;

	@Column(name = "REASON_ID")
	private int reasonId;

	@Column(name = "QC_ACTION_ID")
	private String qcActionId;

	@Column(name = "DIVISION_ID")
	private String divisionId;

	@Column(name = "LINE_ID")
	private String lineId;

	@Column(name = "ASSOCIATE_ID")
	private String associateId;

	@Column(name = "ASSOCIATE_NAME")
	private String associateName;
	
	private static final long serialVersionUID = 1L;

	public HoldReasonMapping() {
		super();
	}

	public int getReasonId() {
		return this.reasonId;
	}

	public void setReasonId(int i) {
		this.reasonId = i;
	}

	public String getQcActionId() {
		return this.qcActionId;
	}

	public void setQcActionId(String id) {
		this.qcActionId = id;
	}

	public String getAssociateName() {
		return StringUtils.trim(this.associateName);
	}

	public void setAssociateName(String associateName) {
		this.associateName = associateName;
	}

	public String getAssociateId() {
		return StringUtils.trim(this.associateId);
	}

	public void setAssociateId(String associateId) {
		this.associateId = associateId;
	}

	public String getDivisionId() {
		return StringUtils.trim(this.divisionId);
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	public String getLineId() {
		return StringUtils.trim(this.lineId);
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public int getReasonMappingId() {
		return this.reasonMappingId;
	}

	public void setReasonMappingId(int reasonMappingId) {
		this.reasonMappingId = reasonMappingId;
	}

	@Override
	public String toString() {
		return toString(getReasonId(), getLineId());
	}

	public Object getId() {
		return reasonMappingId;
	}

}