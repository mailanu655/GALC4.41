package com.honda.galc.client.teamleader.model;

import com.honda.galc.entity.product.PartName;

public class RepairPart {
	
	private PartName partName;
	private Integer sequenceNo;
	
	public PartName getPartName() {
		return partName;
	}
	public int getSequenceNo() {
		return sequenceNo == null?0:sequenceNo.intValue();
	}
	public void setPartName(PartName partName) {
		this.partName = partName;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((partName == null) ? 0 : partName.hashCode());
		result = prime * result + ((sequenceNo == null) ? 0 : sequenceNo.hashCode());
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
		RepairPart other = (RepairPart) obj;
		if (partName == null) {
			if (other.partName != null)
				return false;
		} else if (!partName.equals(other.partName))
			return false;
		if (sequenceNo == null) {
			if (other.sequenceNo != null)
				return false;
		} else if (!sequenceNo.equals(other.sequenceNo))
			return false;
		return true;
	}

	
	

}
