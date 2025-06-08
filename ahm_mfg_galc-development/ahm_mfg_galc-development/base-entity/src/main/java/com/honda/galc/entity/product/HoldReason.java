package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>HoldReason Class description</h3>
 * <p>
 * HoldReason description
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
 *         Dec 1, 2016
 * 
 * 
 */
@Entity
@Table(name = "HOLD_REASON_TBX")
public class HoldReason extends AuditEntry {

	@Id
	@Column(name = "REASON_ID")
	private int reasonId;

	@Column(name = "HOLD_REASON")
	private String holdReason;

	@Column(name = "DIVISION_ID")
	private String divisionId;

	private static final long serialVersionUID = 1L;

	public HoldReason() {
		super();
	}

	public HoldReason(String productId, int holdType) {
		super();
	}

	public int getReasonId() {
		return this.reasonId;
	}

	public void setReasonId(int id) {
		this.reasonId = id;
	}

	public String getHoldReason() {
		return StringUtils.trim(this.holdReason);
	}

	public void setHoldReason(String holdReason) {
		this.holdReason = holdReason;
	}

	public String getDivisionId() {
		return StringUtils.trim(this.divisionId);
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	@Override
	public String toString() {
		return super.toString(getReasonId(), getDivisionId());
	}

	public Object getId() {
		return reasonId;
	}
}