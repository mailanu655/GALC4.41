package com.honda.galc.entity.conf;

import java.io.Serializable;
import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * Feb 10, 2014
 */
@Embeddable
public class MCOperationPartMatrixId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="OPERATION_NAME", nullable=false, length=32)
	private String operationName;

	@Column(name="PART_ID", nullable=false, length=5)
	private String partId;

	@Column(name="PART_REV", nullable=false)
	private int partRevision;

	@Column(name="SPEC_CODE_TYPE", nullable=false, length=32)
	private String specCodeType;

	@Column(name="SPEC_CODE_MASK", nullable=false, length=30)
	private String specCodeMask;

    public MCOperationPartMatrixId() {}
    
	public String getOperationName() {
		return StringUtils.trim(this.operationName);
	}
	
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
	public String getPartId() {
		return StringUtils.trim(this.partId);
	}
	
	public void setPartId(String partId) {
		this.partId = partId;
	}
	
	public int getPartRevision() {
		return this.partRevision;
	}
	
	public void setPartRevision(int partRevision) {
		this.partRevision = partRevision;
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
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MCOperationPartMatrixId)) {
			return false;
		}
		MCOperationPartMatrixId castOther = (MCOperationPartMatrixId)other;
		return 
			this.operationName.equals(castOther.operationName)
			&& this.partId.equals(castOther.partId)
			&& (this.partRevision == castOther.partRevision)
			&& this.specCodeType.equals(castOther.specCodeType)
			&& this.specCodeMask.equals(castOther.specCodeMask);
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.operationName.hashCode();
		hash = hash * prime + this.partId.hashCode();
		hash = hash * prime + this.partRevision;
		hash = hash * prime + this.specCodeType.hashCode();
		hash = hash * prime + this.specCodeMask.hashCode();
		
		return hash;
    }
	
	@Override
	public String toString(){
		return StringUtil.toString(this.getClass().getSimpleName(), getOperationName(), getPartId(), getPartRevision(), getSpecCodeType(),
				getSpecCodeMask());
	}	
}