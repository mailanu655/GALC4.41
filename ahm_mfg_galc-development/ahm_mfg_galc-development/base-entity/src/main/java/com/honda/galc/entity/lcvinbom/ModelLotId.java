package com.honda.galc.entity.lcvinbom;

import java.io.Serializable;
import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * The primary key class for the MODEL_LOT database table.
 * 
 */
@Embeddable
public class ModelLotId implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="MODEL_PART_ID", unique=true, nullable=false)
	private long modelPartId;

	@Column(name="PLAN_CODE", unique=true, nullable=false, length=11)
	private String planCode;

	public ModelLotId() {
	}
	public long getModelPartId() {
		return this.modelPartId;
	}
	public void setModelPartId(long modelPartId) {
		this.modelPartId = modelPartId;
	}
	public String getPlanCode() {
		return StringUtils.trim(this.planCode);
	}
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ModelLotId)) {
			return false;
		}
		ModelLotId castOther = (ModelLotId)other;
		return 
			(this.modelPartId == castOther.modelPartId)
			&& this.planCode.equals(castOther.planCode);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.modelPartId ^ (this.modelPartId >>> 32)));
		hash = hash * prime + this.planCode.hashCode();
		
		return hash;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(),
				getModelPartId(), getPlanCode());
	}
}