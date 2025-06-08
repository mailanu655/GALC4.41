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
public class MCOperationPartRevisionId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="OPERATION_NAME", nullable=false, length=32)
	private String operationName;

	@Column(name="PART_ID", nullable=false, length=5)
	private String partId;

	@Column(name="PART_REV", nullable=false)
	private int partRevision;

    public MCOperationPartRevisionId() {}
    
	public MCOperationPartRevisionId(String operationName, String partId, int partRevision) {
		super();
		this.operationName = operationName;
		this.partId = partId;
		this.partRevision = partRevision;
	}

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

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MCOperationPartRevisionId)) {
			return false;
		}
		MCOperationPartRevisionId castOther = (MCOperationPartRevisionId)other;
		return 
			this.operationName.equals(castOther.operationName)
			&& this.partId.equals(castOther.partId)
			&& (this.partRevision == castOther.partRevision);
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.operationName.hashCode();
		hash = hash * prime + this.partId.hashCode();
		hash = hash * prime + this.partRevision;
		
		return hash;
    }
	
	@Override
	public String toString(){
		return StringUtil.toString(this.getClass().getSimpleName(), getOperationName(), getPartId(), getPartRevision());
	}	
}
