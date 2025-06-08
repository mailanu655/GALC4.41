package com.honda.galc.entity.pdda;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * Apr 09, 2014
 */
@Embeddable
public class UnitImageId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="MAINTENANCE_ID", nullable=false)
	private int maintenanceId;

	@Column(name="IMAGE_SEQ_NO", nullable=false)
	private short imageSeqNo;

	@Column(name="UNIT_IMAGE", nullable=false, length=508)
	private String unitImage;
	
    public UnitImageId() {}
    
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
	
	public String getUnitImage() {
		return this.unitImage;
	}
	
	public void setUnitImage(String unitImage) {
		this.unitImage = unitImage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + imageSeqNo;
		result = prime * result + maintenanceId;
		result = prime * result + ((unitImage == null) ? 0 : unitImage.hashCode());
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
		UnitImageId other = (UnitImageId) obj;
		if (imageSeqNo != other.imageSeqNo)
			return false;
		if (maintenanceId != other.maintenanceId)
			return false;
		if (unitImage == null) {
			if (other.unitImage != null)
				return false;
		} else if (!unitImage.equals(other.unitImage))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getMaintenanceId(), getImageSeqNo(), getUnitImage());
	}
}