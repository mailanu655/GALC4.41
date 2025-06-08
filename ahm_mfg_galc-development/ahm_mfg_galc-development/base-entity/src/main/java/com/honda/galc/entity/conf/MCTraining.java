package com.honda.galc.entity.conf;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * Apr 09, 2014
 */
@Entity
@Table(name="MC_TRAINING_TBX")
public class MCTraining extends AuditEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MCTrainingId id;
	
	@Column(name="EXPIRED")
	private Timestamp expired;
	
	@Column(name="APPROVER_NO", length=11)
	private String approverNo;
	
	@Column(name="TRAINING_METHOD", length=16)
	private String trainingMethod;
	
	@Column(name="COMMENTS", length=250)
	private String comments;
	
	public MCTraining() {}

	public MCTrainingId getId() {
		return id;
	}

	public void setId(MCTrainingId id) {
		this.id = id;
	}

	public Timestamp getExpired() {
		return expired;
	}

	public void setExpired(Timestamp expired) {
		this.expired = expired;
	}

	public String getApproverNo() {
		return StringUtils.trim(approverNo);
	}

	public void setApproverNo(String approverNo) {
		this.approverNo = approverNo;
	}

	public String getTrainingMethod() {
		return StringUtils.trim(trainingMethod);
	}

	public void setTrainingMethod(String trainingMethod) {
		this.trainingMethod = trainingMethod;
	}

	public String getComments() {
		return StringUtils.trim(comments);
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public boolean isExpired(){
		return (this.expired != null)? true : false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((approverNo == null) ? 0 : approverNo.hashCode());
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((expired == null) ? 0 : expired.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((trainingMethod == null) ? 0 : trainingMethod.hashCode());
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
		MCTraining other = (MCTraining) obj;
		if (approverNo == null) {
			if (other.approverNo != null)
				return false;
		} else if (!approverNo.equals(other.approverNo))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (expired == null) {
			if (other.expired != null)
				return false;
		} else if (!expired.equals(other.expired))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (trainingMethod == null) {
			if (other.trainingMethod != null)
				return false;
		} else if (!trainingMethod.equals(other.trainingMethod))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return toString(getId().getProcessPointId(), getId().getAssociateNo(), getId().getPddaPlatformId(),
				getId().getSpecCodeType(), getId().getSpecCodeMask(), getId().getTrained(), getExpired(),
				getApproverNo(), getTrainingMethod(), getComments());
	}
}
