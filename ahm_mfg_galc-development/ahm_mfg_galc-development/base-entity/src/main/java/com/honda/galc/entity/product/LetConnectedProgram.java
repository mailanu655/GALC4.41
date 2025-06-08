package com.honda.galc.entity.product;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>LetConnectedProgram</code> is ... .
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
@Entity
@Table(name = "LET_CONNECTED_PROGRAM_TBX")
public class LetConnectedProgram extends AuditEntry {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private LetConnectedProgramId id;

	@ManyToOne(targetEntity = LetPassCriteriaProgram.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "CRITERIA_PGM_ID", referencedColumnName = "CRITERIA_PGM_ID", updatable = false, insertable = false)
	private LetPassCriteriaProgram program;

	@ManyToOne(targetEntity = LetInspectionProgram.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "TRIGGERING_PGM_ID", referencedColumnName = "INSPECTION_PGM_ID", updatable = false, insertable = false)	
	private LetInspectionProgram triggeringProgram;

	// === constructors === //
	public LetConnectedProgram() {
	}

	public LetConnectedProgram(Integer pgmId, Integer triggeringPgmId) {
		setId(new LetConnectedProgramId(pgmId, triggeringPgmId));
	}

	public LetConnectedProgram(LetConnectedProgramId id) {
		setId(id);
	}

	// === overrides === //
	@Override
	public int hashCode() {
		int prime = 31;
		int hc = 1;
		hc = hc + prime * (getId() == null ? 0 : getId().hashCode());
		return hc;
	}

	// === get/set === //
	public LetConnectedProgramId getId() {
		return id;
	}

	public void setId(LetConnectedProgramId id) {
		this.id = id;
	}

	public LetPassCriteriaProgram getProgram() {
		return program;
	}

	public void setProgram(LetPassCriteriaProgram program) {
		this.program = program;
	}

	public LetInspectionProgram getTriggeringProgram() {
		return triggeringProgram;
	}

	public void setTriggeringProgram(LetInspectionProgram triggeringProgram) {
		this.triggeringProgram = triggeringProgram;
	}
}
