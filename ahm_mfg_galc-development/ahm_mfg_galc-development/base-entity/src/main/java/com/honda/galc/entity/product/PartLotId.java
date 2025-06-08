package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class PartLotId implements Serializable {
	@Column(name="PART_SERIAL_NUMBER")
	private String partSerialNumber;

	@Column(name="PART_NAME")
	private String partName;
	
	@Column(name="PART_NUMBER")
	private String partNumber;


	private static final long serialVersionUID = 1L;

	public PartLotId() {
		super();
	}

	public PartLotId(String partSerialNumber, String partName) {
		this.partSerialNumber = partSerialNumber;
		this.partName = partName;
	}

	public String getPartSerialNumber() {
		return  StringUtils.trim(this.partSerialNumber);
	}

	public void setPartSerialNumber(String partSerialNumber) {
		this.partSerialNumber = partSerialNumber;
	}

	public String getPartName() {
		return  StringUtils.trim(this.partName);
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}
	
	public String getPartNumber() {
		return StringUtils.trim(this.partNumber);
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PartLotId other = (PartLotId) obj;
		if (getPartName() == null) {
			if (other.getPartName() != null)
				return false;
		} else if (!getPartName().equals(other.getPartName()))
			return false;
		if (getPartNumber() == null) {
			if (other.getPartNumber() != null)
				return false;
		} else if (!getPartNumber().equals(other.getPartNumber()))
			return false;
		if (getPartSerialNumber() == null) {
			if (other.getPartSerialNumber() != null)
				return false;
		} else if (!getPartSerialNumber().equals(other.getPartSerialNumber()))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getPartName() == null) ? 0 : getPartName().hashCode());
		result = prime * result
				+ ((getPartNumber() == null) ? 0 : getPartNumber().hashCode());
		result = prime
				* result
				+ ((getPartSerialNumber() == null) ? 0 : getPartSerialNumber().hashCode());
		return result;
	}

}
