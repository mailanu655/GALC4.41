package com.honda.galc.entity.qi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.honda.galc.util.ToStringUtil;
@Embeddable
public class QiPddaResponsibleLoadTriggerId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "PDDA_RESPONSIBILITY_ID")
	private Integer pddaResponsibilityId;
	
	@Column(name = "PCL_TO_QICS_SEQ_KEY")
	private Integer pclToQicsSeqKey;

	public Integer getPddaResponsibilityId() {
		return pddaResponsibilityId;
	}

	public void setPddaResponsibilityId(Integer pddaResponsibilityId) {
		this.pddaResponsibilityId = pddaResponsibilityId;
	}

	public Integer getPclToQicsSeqKey() {
		return pclToQicsSeqKey;
	}

	public void setPclToQicsSeqKey(Integer pclToQicsSeqKey) {
		this.pclToQicsSeqKey = pclToQicsSeqKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pclToQicsSeqKey == null) ? 0 : pclToQicsSeqKey.hashCode());
		result = prime * result + ((pddaResponsibilityId == null) ? 0 : pddaResponsibilityId.hashCode());
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
		QiPddaResponsibleLoadTriggerId other = (QiPddaResponsibleLoadTriggerId) obj;
		if (pclToQicsSeqKey == null) {
			if (other.pclToQicsSeqKey != null)
				return false;
		} else if (!pclToQicsSeqKey.equals(other.pclToQicsSeqKey))
			return false;
		if (pddaResponsibilityId == null) {
			if (other.pddaResponsibilityId != null)
				return false;
		} else if (!pddaResponsibilityId.equals(other.pddaResponsibilityId))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}
}
