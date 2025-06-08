package com.honda.galc.entity.qi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * <h3>QiAppliedRepairMethodId Class description</h3>
 * <p>
 * QiAppliedRepairMethodId description
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
 * @author L&T Infotech<br>
 *         Dec 20, 2016
 *
 *
 */

@Embeddable
public class QiAppliedRepairMethodId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "REPAIR_ID")
	private long repairId;
	
	@Column(name = "REPAIR_METHOD_SEQ")
	private Integer repairMethodSeq;

	public long getRepairId() {
		return repairId;
	}

	public void setRepairId(long repairId) {
		this.repairId = repairId;
	}

	public Integer getRepairMethodSeq() {
		return repairMethodSeq;
	}

	public void setRepairMethodSeq(Integer repairMethodSeq) {
		this.repairMethodSeq = repairMethodSeq;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int)repairId;
		result = prime * result + ((repairMethodSeq == null) ? 0 : repairMethodSeq.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiAppliedRepairMethodId other = (QiAppliedRepairMethodId) obj;
		if (repairId != other.repairId)
			return false;			
		if (repairMethodSeq == null) {
			if (other.repairMethodSeq != null)
				return false;
		} else if (!repairMethodSeq.equals(other.repairMethodSeq))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QiAppliedRepairMethodId [repairId=" + repairId + ", repairMethodSeq=" + repairMethodSeq + "]";
	}
}
