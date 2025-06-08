package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.honda.galc.dto.Auditable;
import org.apache.commons.lang.StringUtils;

@Embeddable
public class MeasurementSpecId implements Serializable {
	@Column(name="PART_NAME")
	@Auditable(isPartOfPrimaryKey= true,sequence=1)
	private String partName;

	@Column(name="PART_ID")
	@Auditable(isPartOfPrimaryKey= true,sequence=2)
	private String partId;

	@Column(name="MEASUREMENT_SEQ_NUM")
	@Auditable(isPartOfPrimaryKey= true,sequence=3)
	private int measurementSeqNum;

	private static final long serialVersionUID = 1L;

	public MeasurementSpecId() {
		super();
	}

	public MeasurementSpecId(String partName, String partId, int seq) {
		super();
		this.partName = partName;
		this.partId = partId;
		this.measurementSeqNum = seq;
	}
	
	public String getPartName() {
		return StringUtils.trim(this.partName);
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getPartId() {
		return StringUtils.trim(this.partId);
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	public int getMeasurementSeqNum() {
		return this.measurementSeqNum;
	}

	public void setMeasurementSeqNum(int measurementSeqNum) {
		this.measurementSeqNum = measurementSeqNum;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof MeasurementSpecId)) {
			return false;
		}
		MeasurementSpecId other = (MeasurementSpecId) o;
		return this.getPartName().equals(other.getPartName())
			&& this.getPartId().equals(other.getPartId())
			&& (this.measurementSeqNum == other.measurementSeqNum);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.partName.hashCode();
		hash = hash * prime + this.partId.hashCode();
		hash = hash * prime + this.measurementSeqNum;
		return hash;
	}
	
	@Override
	public String toString() {
		return  partName + "," + partId + ","
				+ measurementSeqNum;
	}

}
