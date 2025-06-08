package com.honda.galc.entity.pdda;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * Apr 09, 2014
 */
@Embeddable
public class UnitSafetyImageId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="MAINTENANCE_ID", nullable=false)
	private int maintenanceId;

	@Column(name="IMAGE_SEQ_NO", nullable=false)
	private short imageSeqNo;
	
	@Column(name="IMAGE_NAME", nullable=false, length=508)
	private String imageName;

    public UnitSafetyImageId() {}
    
	public int getMaintenanceId() {
		return this.maintenanceId;
	}
	
	public void setMaintenanceId(int maintenanceId) {
		this.maintenanceId = maintenanceId;
	}
	
	public short getImageSeqNo() {
		return this.imageSeqNo;
	}
	
	public void setImageSeqNo(short imageSeqNo) {
		this.imageSeqNo = imageSeqNo;
	}
	
	public String getImageName() {
		return StringUtils.trim(this.imageName);
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + imageSeqNo;
		result = prime * result + maintenanceId;
		result = prime * result
				+ ((imageName == null) ? 0 : imageName.hashCode());
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
		UnitSafetyImageId other = (UnitSafetyImageId) obj;
		if (imageSeqNo != other.imageSeqNo)
			return false;
		if (maintenanceId != other.maintenanceId)
			return false;
		if (imageName == null) {
			if (other.imageName != null)
				return false;
		} else if (!imageName.equals(other.imageName))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getMaintenanceId(), getImageSeqNo(), getImageName());
	}
}