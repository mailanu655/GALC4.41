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
public class MCOperationRevisionId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="OPERATION_NAME", nullable=false, length=32)
	private String operationName;

	@Column(name="OP_REV", nullable=false)
	private int operationRevision;

    public MCOperationRevisionId() {}
    
	public String getOperationName() {
		return StringUtils.trim(this.operationName);
	}
	
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
	public int getOperationRevision() {
		return this.operationRevision;
	}
	
	public void setOperationRevision(int operationRevision) {
		this.operationRevision = operationRevision;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MCOperationRevisionId)) {
			return false;
		}
		MCOperationRevisionId castOther = (MCOperationRevisionId)other;
		return 
			this.operationName.equals(castOther.operationName)
			&& (this.operationRevision == castOther.operationRevision);
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.operationName.hashCode();
		hash = hash * prime + this.operationRevision;
		
		return hash;
    }
	
	@Override
	public String toString(){
		return StringUtil.toString(this.getClass().getSimpleName(), getOperationName(), getOperationRevision());
	}	
}