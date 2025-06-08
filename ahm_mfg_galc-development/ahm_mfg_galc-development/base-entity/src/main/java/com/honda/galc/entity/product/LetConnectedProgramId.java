package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>LetConnectedProgramId</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Feb 1, 2018
 */
@Embeddable
public class LetConnectedProgramId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "CRITERIA_PGM_ID")
	private Integer pgmId;

	@Column(name = "TRIGGERING_PGM_ID")
	private Integer triggeringPgmId;

	// === constructors === //
	public LetConnectedProgramId() {
	}

	public LetConnectedProgramId(Integer pgmId, Integer triggeringPgmId) {
		this.pgmId = pgmId;
		this.triggeringPgmId = triggeringPgmId;
	}

	// === overrides === //
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof LetConnectedProgramId)) {
			return false;
		}
		LetConnectedProgramId other = (LetConnectedProgramId) o;
		Integer pgmId = getPgmId() == null ? 0 : getPgmId();
		Integer trgPgmId = getTriggeringPgmId() == null ? 0 : getTriggeringPgmId();
		return pgmId.equals(other.getPgmId()) && trgPgmId.equals(other.getTriggeringPgmId());
	}

	@Override
	public int hashCode() {
		int prime = 31;
		int hc = 1;
		hc = hc + prime * (getPgmId() == null ? 0 : getPgmId());
		hc = hc + prime * (getTriggeringPgmId() == null ? 0 : getTriggeringPgmId());
		return hc;
	}

	// === get/set === //
	public Integer getPgmId() {
		return pgmId;
	}

	public void setPgmId(Integer pgmId) {
		this.pgmId = pgmId;
	}

	public Integer getTriggeringPgmId() {
		return triggeringPgmId;
	}

	public void setTriggeringPgmId(Integer triggeringPgmId) {
		this.triggeringPgmId = triggeringPgmId;
	}
}
