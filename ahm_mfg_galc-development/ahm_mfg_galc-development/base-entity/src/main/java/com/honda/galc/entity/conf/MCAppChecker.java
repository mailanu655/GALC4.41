package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Column;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.checkers.ReactionType;
import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * @date Sep 30, 2014
 */

@Entity
@Table(name="MC_APP_CHECKER_TBX")
public class MCAppChecker extends AuditEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MCAppCheckerId id;
	
	@Column(name="CHECK_NAME", nullable=false)
	private String checkName;
	
	@Column(name="CHECKER", nullable=false)
	private String checker;
	
	@Column(name="REACTION_TYPE")
    @Enumerated(EnumType.STRING)
	private ReactionType reactionType;
	
	public MCAppChecker() {}
	
	public String getCheckName() {
		return StringUtils.trim(checkName);
	}

	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}

	public String getChecker() {
		return StringUtils.trim(checker);
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

	public ReactionType getReactionType() {
		return reactionType;
	}

	public void setReactionType(ReactionType reactionType) {
		this.reactionType = reactionType;
	}

	public MCAppCheckerId getId() {
		return id;
	}

	public void setId(MCAppCheckerId id) {
		this.id = id;
	}

	public int getCheckSeq() {
		return this.getId().getCheckSeq();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((checkName == null) ? 0 : checkName.hashCode());
		result = prime * result + ((checker == null) ? 0 : checker.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((reactionType == null) ? 0 : reactionType.hashCode());
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
		MCAppChecker other = (MCAppChecker) obj;
		if (checkName == null) {
			if (other.checkName != null)
				return false;
		} else if (!checkName.equals(other.checkName))
			return false;
		if (checker == null) {
			if (other.checker != null)
				return false;
		} else if (!checker.equals(other.checker))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (reactionType == null) {
			if (other.reactionType != null)
				return false;
		} else if (!reactionType.equals(other.reactionType))
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return toString(getId().getApplicationId(), getId().getCheckPoint(), getId().getCheckSeq(), 
				getCheckName(), getChecker(), getReactionType());
	}
}
