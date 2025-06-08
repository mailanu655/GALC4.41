package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * <h3>Class description</h3>
 * Defect IQS Score
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Oct. 16, 2019</TD>
 * <TD>1.0</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */

@Entity
@Table(name = "QI_DEFECT_IQS_SCORE_TBX")
public class QiDefectIqsScore extends AuditEntry {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "DEFECTRESULTID", nullable=false)
	private long defectResultId;
	
	@Column(name="IQS_SCORE")
	private double iqsScore;
	
	@Column(name="IQS_AUDIT_ACTION")
	private short iqsAuditAction;
	
	public QiDefectIqsScore() {
		super();
	}
	
	public QiDefectIqsScore(QiDefectResult defectResult) {
		if(defectResult != null) {
			this.defectResultId = defectResult.getDefectResultId();
			this.iqsScore = defectResult.getIqsScore();
			this.iqsAuditAction = defectResult.getIqsAuditAction();
		}
	}
	
	public Object getId() {
		return getDefectResultId();
	}

	public long getDefectResultId() {
		return defectResultId;
	}

	public void setDefectResultId(long defectResultId) {
		this.defectResultId = defectResultId;
	}
	
	public double getIqsScore() {
		return iqsScore;
	}

	public void setIqsScore(double iqsScore) {
		this.iqsScore = iqsScore;
	}

	public short getIqsAuditAction() {
		return iqsAuditAction;
	}

	public void setIqsAuditAction(short iqsAuditAction) {
		this.iqsAuditAction = iqsAuditAction;
	}

	public String toString() {
		return toString(getId(), getIqsScore());
	}

}
