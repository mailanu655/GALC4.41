package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * @date Sep 30, 2014
 */
@Embeddable
public class MCPartCheckerId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="OPERATION_NAME", nullable=false, length=32)
	private String operationName;
	
	@Column(name="PART_ID", nullable=false, length=5)
	private String partId;
	
	@Column(name="OP_REV", nullable=false)
	private int operationRevision;

	@Column(name="CHECK_POINT", nullable=false)
	private String checkPoint;

	@Column(name="CHECK_SEQ", nullable=false)
	private int checkSeq;
	
	public MCPartCheckerId() {} 
	
	public MCPartCheckerId(String operationName, String partId, int operationRevision, String checkPoint, int checkSeq) {
		this.operationName = operationName;
		this.partId = partId;
		this.operationRevision = operationRevision;
		this.checkPoint = checkPoint;
		this.checkSeq = checkSeq;
	}

	public String getOperationName() {
		return StringUtils.trim(operationName);
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
	public String getPartId() {
		return StringUtils.trim(partId);
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	public int getOperationRevision() {
		return operationRevision;
	}

	public void setOperationRevision(int operationRevision) {
		this.operationRevision = operationRevision;
	}
	
	public String getCheckPoint() {
		return StringUtils.trim(checkPoint);
	}

	public void setCheckPoint(String checkPoint) {
		this.checkPoint = checkPoint;
	}

	public int getCheckSeq() {
		return checkSeq;
	}

	public void setCheckSeq(int checkSeq) {
		this.checkSeq = checkSeq;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((checkPoint == null) ? 0 : checkPoint.hashCode());
		result = prime * result + checkSeq;
		result = prime * result
				+ ((operationName == null) ? 0 : operationName.hashCode());
		result = prime * result + operationRevision;
		result = prime * result + ((partId == null) ? 0 : partId.hashCode());
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
		MCPartCheckerId other = (MCPartCheckerId) obj;
		if (checkPoint == null) {
			if (other.checkPoint != null)
				return false;
		} else if (!checkPoint.equals(other.checkPoint))
			return false;
		if (checkSeq != other.checkSeq)
			return false;
		if (operationName == null) {
			if (other.operationName != null)
				return false;
		} else if (!operationName.equals(other.operationName))
			return false;
		if (operationRevision != other.operationRevision)
			return false;
		if (partId == null) {
			if (other.partId != null)
				return false;
		} else if (!partId.equals(other.partId))
			return false;
		return true;
	}

	@Override
	public String toString(){
		return StringUtil.toString(this.getClass().getSimpleName(), getOperationName(), getPartId(),
				getOperationRevision(), getCheckPoint(), getCheckSeq());
	}
}