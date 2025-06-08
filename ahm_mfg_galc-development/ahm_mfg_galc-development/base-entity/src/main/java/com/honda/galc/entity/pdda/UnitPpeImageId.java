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
public class UnitPpeImageId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="MAINTENANCE_ID", nullable=false)
	private int maintenanceId;

	@Column(name="IMAGE_SEQ_NO", nullable=false)
	private short imageSeqNo;

	@Column(name="PPE_ID", nullable=false)
	private int ppeId;
	
    public UnitPpeImageId() {}
    
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
	
	public int getPpeId() {
		return this.ppeId;
	}

	public void setPpeId(int ppeId) {
		this.ppeId = ppeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + imageSeqNo;
		result = prime * result + maintenanceId;
		result = prime * result + ppeId;
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
		UnitPpeImageId other = (UnitPpeImageId) obj;
		if (imageSeqNo != other.imageSeqNo)
			return false;
		if (maintenanceId != other.maintenanceId)
			return false;
		if (ppeId != other.ppeId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getMaintenanceId(), getImageSeqNo(), getPpeId());
	}
}