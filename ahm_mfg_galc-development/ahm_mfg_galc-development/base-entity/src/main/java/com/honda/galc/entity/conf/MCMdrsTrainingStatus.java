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
 * The persistent class for the MC_MDRS_TRAINING_STATUS_TBX database table.
 * 
 */
@Entity
@Table(name="MC_MDRS_TRAINING_STATUS_TBX")
public class MCMdrsTrainingStatus extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MCMdrsTrainingStatusId id;

	@Column(name="ASSOCIATE_NO", length=7)
	private String associateNo;

	@Column(name="PROCESS_POINT_ID", length=16)
	private String processPointId;

	@Column(name="SPEC_CODE_MASK", length=30)
	private String specCodeMask;

	@Column(name="SPEC_CODE_TYPE", length=32)
	private String specCodeType;

	@Column(name="TRAINED")
	private Timestamp trained;

	public MCMdrsTrainingStatus() {
	}

	public MCMdrsTrainingStatusId getId() {
		return this.id;
	}

	public void setId(MCMdrsTrainingStatusId id) {
		this.id = id;
	}

	public String getAssociateNo() {
		return StringUtils.trim(this.associateNo);
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}

	public String getProcessPointId() {
		return StringUtils.trim(this.processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getSpecCodeMask() {
		return StringUtils.trim(this.specCodeMask);
	}

	public void setSpecCodeMask(String specCodeMask) {
		this.specCodeMask = specCodeMask;
	}

	public String getSpecCodeType() {
		return StringUtils.trim(this.specCodeType);
	}

	public void setSpecCodeType(String specCodeType) {
		this.specCodeType = specCodeType;
	}

	public Timestamp getTrained() {
		return this.trained;
	}

	public void setTrained(Timestamp trained) {
		this.trained = trained;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((associateNo == null) ? 0 : associateNo.hashCode());
		result = prime * result
				+ ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((specCodeMask == null) ? 0 : specCodeMask.hashCode());
		result = prime * result
				+ ((specCodeType == null) ? 0 : specCodeType.hashCode());
		result = prime * result
				+ ((trained == null) ? 0 : trained.hashCode());
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
		MCMdrsTrainingStatus other = (MCMdrsTrainingStatus) obj;
		if (associateNo == null) {
			if (other.associateNo != null)
				return false;
		} else if (!associateNo.equals(other.associateNo))
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (specCodeMask == null) {
			if (other.specCodeMask != null)
				return false;
		} else if (!specCodeMask.equals(other.specCodeMask))
			return false;
		if (specCodeType == null) {
			if (other.specCodeType != null)
				return false;
		} else if (!specCodeType.equals(other.specCodeType))
			return false;
		if (trained == null) {
			if (other.trained != null)
				return false;
		} else if (!trained.equals(other.trained))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return toString(getId().getTrainingStatusId(), getId().getPddaPlatformId(), getProcessPointId()
				, getAssociateNo(), getSpecCodeMask(), getSpecCodeType(), getTrained());
	}
}