package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * Feb 10, 2014
 * 
 * @author Fredrick Yessaian
 * Mar 04, 2015 :: Modified to include division id
 */
@Embeddable
public class MCStructureId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="PRODUCT_SPEC_CODE", nullable=false, length=30)
	private String productSpecCode;

	@Column(name="STRUCTURE_REV", nullable=false)
	private long revision;

	@Column(name="PROCESS_POINT_ID", nullable=false, length=16)
	private String processPointId;

	@Column(name="OPERATION_NAME", nullable=false, length=32)
	private String operationName;

	@Column(name="OP_REV", nullable=false)
	private int operationRevision;

	@Column(name="PDDA_PLATFORM_ID", nullable=false)
	private int pddaPlatformId;

	@Column(name="PART_ID", nullable=false, length=5)
	private String partId;

	@Column(name="PART_REV", nullable=false)
	private int partRevision;
	
	@Column(name="DIVISION_ID", nullable=false, length=16)
	private String divisionId;
	
    public MCStructureId() {}
    
    public MCStructureId(String productSpecCode, long revision,
    		String processPointId, String operationName, 
    		int operationRevision, String partId, int partRevision, int pddaPlatformId, String divisionId) {
    	this.productSpecCode = productSpecCode;
    	this.revision = revision;
    	this.processPointId = processPointId;
    	this.operationName = operationName;
    	this.operationRevision = operationRevision;
    	this.pddaPlatformId = pddaPlatformId;
    	this.partId = partId;
    	this.partRevision = partRevision;
    	this.divisionId = divisionId;
    }
    
	public String getProductSpecCode() {
		return StringUtils.trim(this.productSpecCode);
	}
	
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
	
	public long getRevision() {
		return this.revision;
	}
	
	public void setRevision(long revision) {
		this.revision = revision;
	}
	
	public String getProcessPointId() {
		return StringUtils.trim(this.processPointId);
	}
	
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	
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

	public int getPddaPlatformId() {
		return pddaPlatformId;
	}
	
	public void setPddaPlatformId(int pddaPlatformId) {
		this.pddaPlatformId = pddaPlatformId;
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
	
	public String getDivisionId() {
		return StringUtils.trim(divisionId);
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	@Override
	public String toString(){
		return StringUtil.toString(this.getClass().getSimpleName(), getProductSpecCode(), getRevision(), 
				getProcessPointId(), getOperationName(), getOperationRevision(), getPddaPlatformId(),
				getPartId(), getPartRevision(), getDivisionId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((divisionId == null) ? 0 : divisionId.hashCode());
		result = prime * result
				+ ((operationName == null) ? 0 : operationName.hashCode());
		result = prime * result + operationRevision;
		result = prime * result + ((partId == null) ? 0 : partId.hashCode());
		result = prime * result + partRevision;
		result = prime * result + pddaPlatformId;
		result = prime * result
				+ ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result
				+ ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + (int) (revision ^ (revision >>> 32));
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
		MCStructureId other = (MCStructureId) obj;
		if (divisionId == null) {
			if (other.divisionId != null)
				return false;
		} else if (!divisionId.equals(other.divisionId))
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
		if (partRevision != other.partRevision)
			return false;
		if (pddaPlatformId != other.pddaPlatformId)
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		if (productSpecCode == null) {
			if (other.productSpecCode != null)
				return false;
		} else if (!productSpecCode.equals(other.productSpecCode))
			return false;
		if (revision != other.revision)
			return false;
		return true;
	}

}