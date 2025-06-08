package com.honda.galc.entity.conf;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.openjpa.persistence.jdbc.ElementJoinColumn;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumns;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * Feb 10, 2014
 * 
 * @author Fredrick Yessaian
 * Mar 04, 2015 :: Modified to include division id
 */
@Entity
@Table(name="MC_STRUCTURE_TBX")
public class MCStructure extends AuditEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MCStructureId id;
	
	@Column(name="PROCESS_SEQ_NUM", nullable=false)
	private int processSeqNum;

	@OneToOne(fetch=FetchType.EAGER)
	 @JoinColumns({
		 @JoinColumn(name="OPERATION_NAME", referencedColumnName="OPERATION_NAME", updatable = false, insertable=false),
		 @JoinColumn(name="OP_REV", referencedColumnName="OP_REV", updatable = false, insertable=false),
		 @JoinColumn(name="PDDA_PLATFORM_ID", referencedColumnName="PDDA_PLATFORM_ID", updatable = false, insertable=false)
	 })	
	@OrderBy
	private MCOperationRevPlatform operationRevisionPlatform;
	
	@OneToOne(fetch=FetchType.EAGER)
	 @JoinColumns({
		 @JoinColumn(name="OPERATION_NAME", referencedColumnName="OPERATION_NAME", updatable = false, insertable=false),
		 @JoinColumn(name="OP_REV", referencedColumnName="OP_REV", updatable = false, insertable=false),
	 })	
	@OrderBy
	private MCOperationRevision mcOperationRevision;
	
	@OneToMany(fetch=FetchType.EAGER)
	 @ElementJoinColumns({
		 @ElementJoinColumn(name="OPERATION_NAME", referencedColumnName="OPERATION_NAME", updatable = false, insertable=false),
		 @ElementJoinColumn(name="OP_REV", referencedColumnName="OP_REV", updatable = false, insertable=false),
		 @ElementJoinColumn(name="PDDA_PLATFORM_ID", referencedColumnName="PDDA_PLATFORM_ID", updatable = false, insertable=false)	 
	 })	
	@OrderBy
	private List<MCPddaUnitRevision> pddaUnitRevisions;

	public MCStructure() {}

	public MCStructureId getId() {
		return this.id;
	}

	public void setId(MCStructureId id) {
		this.id = id;
	}
		
	public MCOperationRevPlatform getOperationRevisionPlatform() {
		return operationRevisionPlatform;
	}
	
	public void setOperationRevisionPlatform(MCOperationRevPlatform operationRevisionPlatform) {
		this.operationRevisionPlatform = operationRevisionPlatform;
	}
	
	public MCOperationRevision getMcOperationRevision() {
		return mcOperationRevision;
	}

	public void setMcOperationRevision(MCOperationRevision mcOperationRevision) {
		this.mcOperationRevision = mcOperationRevision;
	}

	public List<MCPddaUnitRevision> getPddaUnitRevisions() {
		return pddaUnitRevisions;
	}

	public void setPddaUnitRevisions(List<MCPddaUnitRevision> pddaUnitRevisions) {
		this.pddaUnitRevisions = pddaUnitRevisions;
	}

	public int getOperationSeqNumber() {
		return this.getOperationRevisionPlatform().getOperationSeqNum();
	}
	
	public int getProcessSeqNum() {
		return processSeqNum;
	}

	public void setProcessSeqNum(int processSeqNum) {
		this.processSeqNum = processSeqNum;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((operationRevisionPlatform == null) ? 0
						: operationRevisionPlatform.hashCode());
		result = prime
				* result
				+ ((mcOperationRevision == null) ? 0 : mcOperationRevision.hashCode());
		result = prime * result + processSeqNum;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MCStructure other = (MCStructure) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (operationRevisionPlatform == null) {
			if (other.operationRevisionPlatform != null)
				return false;
		} else if (!operationRevisionPlatform
				.equals(other.operationRevisionPlatform))
			return false;
		if (mcOperationRevision == null) {
			if (other.mcOperationRevision != null)
				return false;
		} else if (!mcOperationRevision.equals(other.mcOperationRevision))
			return false;
		if (processSeqNum != other.processSeqNum)
			return false;
		return true;
	}

	@Override
	public String toString(){
		return toString(getId().getProductSpecCode(), getId().getRevision(), getId().getProcessPointId(), 
				getId().getOperationName(), getId().getOperationRevision(),getId().getPddaPlatformId(), getId().getPartId(), getId().getPartRevision(), getId().getDivisionId(),
				getOperationRevisionPlatform(), getMcOperationRevision(), getProcessSeqNum());
	}
}
