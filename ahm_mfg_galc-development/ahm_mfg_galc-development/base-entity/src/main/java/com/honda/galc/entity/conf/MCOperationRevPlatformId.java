package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * @date Apr 8, 2014
 */
@Embeddable
public class MCOperationRevPlatformId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="OPERATION_NAME", nullable=false, length=32)
	private String operationName;

	@Column(name="OP_REV", nullable=false)
	private int operationRevision;

	@Column(name="PDDA_PLATFORM_ID", nullable=false)
	private int pddaPlatformId;
	
	public MCOperationRevPlatformId() {}
	
	public String getOperationName() {
		return StringUtils.trim(this.operationName);
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public int getOperationRevision() {
		return operationRevision;
	}

	public void setOperationRevision(int operationRevision) {
		this.operationRevision = operationRevision;
	}
	
	public int getPddaPlatformId() {
		return pddaPlatformId;
	}
	
	public void setPddaPlatformId(int pddaPlatformId) {
		this.pddaPlatformId = pddaPlatformId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((operationName == null) ? 0 : operationName.hashCode());
		result = prime * result + operationRevision;
		result = prime * result + pddaPlatformId;
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
		MCOperationRevPlatformId other = (MCOperationRevPlatformId) obj;
		if (operationName == null) {
			if (other.operationName != null)
				return false;
		} else if (!operationName.equals(other.operationName))
			return false;
		if (operationRevision != other.operationRevision)
			return false;
		if (pddaPlatformId != other.pddaPlatformId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getOperationName(), getOperationRevision(), getPddaPlatformId());
	}
}
