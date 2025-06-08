package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * Feb 10, 2014
 */
@Embeddable
public class MCOperationMatrixId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="OPERATION_NAME", nullable=false, length=32)
	private String operationName;

	@Column(name="OP_REV", nullable=false)
	private int operationRevision;

	@Column(name="PDDA_PLATFORM_ID", nullable=false)
	private int pddaPlatformId;
	
	@Column(name="SPEC_CODE_TYPE", nullable=false, length=32)
	private String specCodeType;

	@Column(name="SPEC_CODE_MASK", nullable=false, length=30)
	private String specCodeMask;

    public MCOperationMatrixId() {}
    
	public String getOperationName() {
		return this.operationName;
	}
	
	public MCOperationMatrixId(String operationName, Integer opRev, Integer platFormId, String specCodeType, String specCodeMask){
		this.setOperationName(operationName);
		this.setOperationRevision(opRev);
		this.setPddaPlatformId(platFormId);
		this.setSpecCodeType(specCodeType);
		this.setSpecCodeMask(specCodeMask);
	}
	
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
	public Integer getOperationRevision() {
		return this.operationRevision;
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
	
	public String getSpecCodeType() {
		return StringUtils.trim(this.specCodeType);
	}
	
	public void setSpecCodeType(String specCodeType) {
		this.specCodeType = specCodeType;
	}
	
	public String getSpecCodeMask() {
		return StringUtils.trim(this.specCodeMask);
	}
	
	public void setSpecCodeMask(String specCodeMask) {
		this.specCodeMask = specCodeMask;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((operationName == null) ? 0 : operationName.hashCode());
		result = prime * result + operationRevision;
		result = prime * result + pddaPlatformId;
		result = prime * result
				+ ((specCodeMask == null) ? 0 : specCodeMask.hashCode());
		result = prime * result
				+ ((specCodeType == null) ? 0 : specCodeType.hashCode());
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
		MCOperationMatrixId other = (MCOperationMatrixId) obj;
		if (operationName == null) {
			if (other.operationName != null)
				return false;
		} else if (!operationName.equals(other.operationName))
			return false;
		if (operationRevision != other.operationRevision)
			return false;
		if (pddaPlatformId != other.pddaPlatformId)
			return false;
		if (specCodeMask == null) {
			if (other.specCodeMask != null)
				return false;
		} else if (!specCodeMask.equals(other.specCodeMask))
			return false;
		if (specCodeType == null) {
			if (other.specCodeType != null)
				return false;
		} else if (!specCodeType.equals(other.specCodeType))
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return StringUtil.toString(this.getClass().getSimpleName(), getOperationName(), getOperationRevision(), 
				getPddaPlatformId(), getSpecCodeType(), getSpecCodeMask());
	}
}