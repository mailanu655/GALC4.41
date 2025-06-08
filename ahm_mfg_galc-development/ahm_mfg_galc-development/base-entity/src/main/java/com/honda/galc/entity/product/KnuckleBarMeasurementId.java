package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/** * * 
* @version 0.1 
* @author Fredrick Yessaian 
* @since Oct 19, 2012
*/

@Embeddable
public class KnuckleBarMeasurementId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "PART_NAME")
	private String partName;

	@Column(name = "PART_ID")
	private String partId;

	@Column(name = "PART_SERIAL_NUMBER")
	private String partSerialNumber;

	@Column(name = "MEASUREMENT_SEQUENCE_NUMBER")
	private int measurementSequenceNumber;

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

	public String getPartSerialNumber() {
		return StringUtils.trim(this.partSerialNumber);
	}

	public void setPartSerialNumber(String partSerialNumber) {
		this.partSerialNumber = partSerialNumber;
	}

	public int getMeasurementSequenceNumber() {
		return this.measurementSequenceNumber;
	}

	public void setMeasurementSequenceNumber(int measurementSequenceNumber) {
		this.measurementSequenceNumber = measurementSequenceNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + measurementSequenceNumber;
		result = prime * result + ((partId == null) ? 0 : partId.hashCode());
		result = prime * result
				+ ((partName == null) ? 0 : partName.hashCode());
		result = prime
				* result
				+ ((partSerialNumber == null) ? 0 : partSerialNumber.hashCode());
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
		KnuckleBarMeasurementId other = (KnuckleBarMeasurementId) obj;
		if (measurementSequenceNumber != other.measurementSequenceNumber)
			return false;
		if (partId == null) {
			if (other.partId != null)
				return false;
		} else if (!partId.equals(other.partId))
			return false;
		if (partName == null) {
			if (other.partName != null)
				return false;
		} else if (!partName.equals(other.partName))
			return false;
		if (partSerialNumber == null) {
			if (other.partSerialNumber != null)
				return false;
		} else if (!partSerialNumber.equals(other.partSerialNumber))
			return false;
		return true;
	}

}
