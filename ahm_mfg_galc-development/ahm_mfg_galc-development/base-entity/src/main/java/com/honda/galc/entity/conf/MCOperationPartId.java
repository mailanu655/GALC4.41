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
public class MCOperationPartId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="OPERATION_NAME", nullable=false, length=32)
	private String operationName;

	@Column(name="PART_ID", nullable=false, length=5)
	private String partId;

    public MCOperationPartId() {}
    
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

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MCOperationPartId)) {
			return false;
		}
		MCOperationPartId castOther = (MCOperationPartId)other;
		return 
			this.operationName.equals(castOther.operationName)
			&& this.partId.equals(castOther.partId);
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.operationName.hashCode();
		hash = hash * prime + this.partId.hashCode();
		
		return hash;
    }

	@Override
	public String toString(){
		return StringUtil.toString(this.getClass().getSimpleName(), getOperationName(), getPartId());
	}
}