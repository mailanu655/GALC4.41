package com.honda.galc.entity.conf;

import java.io.Serializable;
import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * The primary key class for the MASS_MESSAGE_TBX database table.
 * 
 */
@Embeddable
public class MassMessageId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "PLANT_NAME")
	private String plantName;

	@Column(name = "DEPARTMENT_ID")
	private String departmentId;

	@Column(name = "LINE_ID")
	private String lineId;

	@Column(name = "MASS_MESSAGE_TYPE")
	private short massMessageType;

	public MassMessageId() {
	}

	public MassMessageId(String plantName, String departmentId, String lineId, int massMessageType){
		this.setPlantName(plantName);
		this.setDepartmentId(departmentId);
		this.setLineId(lineId);
		this.setMassMessageType((short)massMessageType);
	}
	public String getPlantName() {
		return StringUtils.trim(this.plantName);
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public String getDepartmentId() {
		return StringUtils.trim(this.departmentId);
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getLineId() {
		return StringUtils.trim(this.lineId);
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public short getMassMessageType() {
		return this.massMessageType;
	}

	public void setMassMessageType(short massMessageType) {
		this.massMessageType = massMessageType;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MassMessageId)) {
			return false;
		}
		MassMessageId castOther = (MassMessageId) other;
		return this.plantName.equals(castOther.plantName)
				&& this.departmentId.equals(castOther.departmentId)
				&& this.lineId.equals(castOther.lineId)
				&& (this.massMessageType == castOther.massMessageType);

	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.plantName.hashCode();
		hash = hash * prime + this.departmentId.hashCode();
		hash = hash * prime + this.lineId.hashCode();
		hash = hash * prime + ((int) this.massMessageType);

		return hash;
	}

	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(),
				getPlantName(), getDepartmentId(), getLineId(),
				getMassMessageType());
	}

}