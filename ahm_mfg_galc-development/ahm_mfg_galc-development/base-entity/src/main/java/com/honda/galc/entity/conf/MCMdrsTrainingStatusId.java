package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.honda.galc.util.StringUtil;

/**
 * The primary key class for the MC_MDRS_TRAINING_STATUS_TBX database table.
 * 
 */
@Embeddable
public class MCMdrsTrainingStatusId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="TRAINING_STATUS_ID")
	private long trainingStatusId;

	@Column(name="PDDA_PLATFORM_ID")
	private int pddaPlatformId;

	public MCMdrsTrainingStatusId() {
	}
	public long getTrainingStatusId() {
		return this.trainingStatusId;
	}
	public void setTrainingStatusId(long trainingStatusId) {
		this.trainingStatusId = trainingStatusId;
	}
	public int getPddaPlatformId() {
		return this.pddaPlatformId;
	}
	public void setPddaPlatformId(int pddaPlatformId) {
		this.pddaPlatformId = pddaPlatformId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MCMdrsTrainingStatusId other = (MCMdrsTrainingStatusId) obj;
		if (pddaPlatformId != other.pddaPlatformId)
			return false;
		if (trainingStatusId != other.trainingStatusId)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ pddaPlatformId;
		result = prime * result
				+ ((int) (trainingStatusId ^ (trainingStatusId >>> 32)));
		return result;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getPddaPlatformId()
				, getTrainingStatusId());
	}
}