package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * @date Apr 8, 2014
 */
@Embeddable
public class MCPddaChangeId implements Serializable {

	private static final long serialVersionUID = 1L;
	 
	@Column(name="REV_ID", nullable=false)
	private long revisionId;

	@Column(name="CHANGE_FORM_ID", nullable=false)
	private int changeFormId;
	
	public MCPddaChangeId() {}
	
	public long getRevisionId() {
		return revisionId;
	}

	public void setRevisionId(long revisionId) {
		this.revisionId = revisionId;
	}

	public int getChangeFormId() {
		return changeFormId;
	}

	public void setChangeFormId(int changeFormId) {
		this.changeFormId = changeFormId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + changeFormId;
		result = prime * result + (int) (revisionId ^ (revisionId >>> 32));
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
		MCPddaChangeId other = (MCPddaChangeId) obj;
		if (changeFormId != other.changeFormId)
			return false;
		if (revisionId != other.revisionId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getRevisionId(), getChangeFormId());
	}
}
