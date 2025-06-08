package com.honda.galc.entity.product;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;


@Entity
@Table(name = "RULE_EXCLUSION_TBX")
public class RuleExclusion extends AuditEntry{
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RuleExclusionId id;
	
	public RuleExclusion() {
		super();
	}
	
	public RuleExclusionId getId() {
		return this.id;
	}
	
	public void setId(RuleExclusionId id) {
		this.id = id;
	}
	
	public String getProductSpecCode() {
		return id.getProductSpecCode();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
}