package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.entity.AuditEntry;


/*
 * 
 * @author Gangadhararao Gadde 
 * @since Feb 06, 2014
 */
@Entity
@Table(name="GAL225TBX")
public class Rule extends AuditEntry {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="RULE_ID")
	private String  id;

	@Column(name="ACTIVE_STATE")
	private Integer activeState;

	@Column(name="RULE_DESCRIPTION")
	private String ruleDesc;

	@Column(name="PROCESS_POINT_ID")
	private String processPointId;

	public Rule() {
		super();
	}

	public Rule(String id, Integer activeState, String ruleDesc,String processPointId) {
		super();
		this.id = id;
		this.activeState = activeState;
		this.ruleDesc = ruleDesc;
		this.processPointId = processPointId;
	}

	public Integer getActiveState() {
		return activeState;
	}

	public void setActiveState(Integer activeState) {
		this.activeState = activeState;
	}

	public String getRuleDesc() {
		return StringUtils.trimToEmpty(ruleDesc);
	}

	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}

	public String getProcessPointId() {
		return StringUtils.trimToEmpty(processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getId() {
		return StringUtils.trimToEmpty(id);
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return toString(getId(),getActiveState(),getRuleDesc(),getProcessPointId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
		+ ((activeState == null) ? 0 : activeState.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
		+ ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result
		+ ((ruleDesc == null) ? 0 : ruleDesc.hashCode());
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
		Rule other = (Rule) obj;
		if (activeState == null) {
			if (other.activeState != null)
				return false;
		} else if (!activeState.equals(other.activeState))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		if (ruleDesc == null) {
			if (other.ruleDesc != null)
				return false;
		} else if (!ruleDesc.equals(other.ruleDesc))
			return false;
		return true;
	}

}
