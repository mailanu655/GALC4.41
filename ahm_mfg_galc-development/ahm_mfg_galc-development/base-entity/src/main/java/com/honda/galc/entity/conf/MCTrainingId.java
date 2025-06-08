package com.honda.galc.entity.conf;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * @date May 27, 2014
 */
@Embeddable
public class MCTrainingId implements Serializable {
		
	private static final long serialVersionUID = 1L;

	@Column(name="PROCESS_POINT_ID", nullable=false, length=16)
	private String processPointId;

	@Column(name="ASSOCIATE_NO", nullable=false, length=11)
	private String associateNo;

	@Column(name="PDDA_PLATFORM_ID", nullable=false)
	private int pddaPlatformId;
	
	@Column(name="SPEC_CODE_TYPE", nullable=false, length=32)
	private String specCodeType;
	
	@Column(name="SPEC_CODE_MASK", nullable=false, length=30)
	private String specCodeMask;
	
	@Column(name="TRAINED",nullable=false)
	private Timestamp trained;
	
	public MCTrainingId() {}

	public String getProcessPointId() {
		return StringUtils.trim(processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getAssociateNo() {
		return StringUtils.trim(associateNo);
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}

	public int getPddaPlatformId() {
		return pddaPlatformId;
	}

	public void setPddaPlatformId(int pddaPlatformId) {
		this.pddaPlatformId = pddaPlatformId;
	}

	public String getSpecCodeType() {
		return StringUtils.trim(specCodeType);
	}

	public void setSpecCodeType(String specCodeType) {
		this.specCodeType = specCodeType;
	}

	public String getSpecCodeMask() {
		return StringUtils.trim(specCodeMask);
	}

	public void setSpecCodeMask(String specCodeMask) {
		this.specCodeMask = specCodeMask;
	}

	public Timestamp getTrained() {
		return trained;
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
		result = prime * result + pddaPlatformId;
		result = prime * result
				+ ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result
				+ ((specCodeMask == null) ? 0 : specCodeMask.hashCode());
		result = prime * result
				+ ((specCodeType == null) ? 0 : specCodeType.hashCode());
		result = prime * result + ((trained == null) ? 0 : trained.hashCode());
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
		MCTrainingId other = (MCTrainingId) obj;
		if (associateNo == null) {
			if (other.associateNo != null)
				return false;
		} else if (!associateNo.equals(other.associateNo))
			return false;
		if (pddaPlatformId != other.pddaPlatformId)
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
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
		return StringUtil.toString(this.getClass().getSimpleName(), getProcessPointId(), getAssociateNo(),
				getPddaPlatformId(), getSpecCodeType(), getSpecCodeMask(), getTrained());
	}
}
