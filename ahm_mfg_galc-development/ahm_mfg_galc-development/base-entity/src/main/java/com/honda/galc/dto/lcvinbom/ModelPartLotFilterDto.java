package com.honda.galc.dto.lcvinbom;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;


public class ModelPartLotFilterDto implements IDto {
	private static final long serialVersionUID = 1L;
	
	
	@DtoTag()
	private String letSystemName;
	@DtoTag()
	private String dcPartNumber;
	@DtoTag()
	private String dcNumber;
	
	
	public String getLetSystemName() {
		return letSystemName;
	}
	public void setLetSystemNames(String letSystemNameFilter) {
		this.letSystemName = letSystemNameFilter;
	}
	public String getDcPartNumber() {
		return dcPartNumber;
	}
	public void setDcPartNumber(String dcPartNumberFilter) {
		this.dcPartNumber = dcPartNumberFilter;
	}
	public String getDcNumber() {
		return dcNumber;
	}
	public void setDcNumbers(String dcNumberFilter) {
		this.dcNumber = dcNumberFilter;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dcNumber == null) ? 0 : dcNumber.hashCode());
		result = prime * result + ((dcPartNumber == null) ? 0 : dcPartNumber.hashCode());
		result = prime * result + ((letSystemName == null) ? 0 : letSystemName.hashCode());
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
		ModelPartLotFilterDto other = (ModelPartLotFilterDto) obj;
		if (dcNumber == null) {
			if (other.dcNumber != null)
				return false;
		} else if (!dcNumber.equals(other.dcNumber))
			return false;
		if (dcPartNumber == null) {
			if (other.dcPartNumber != null)
				return false;
		} else if (!dcPartNumber.equals(other.dcPartNumber))
			return false;
		if (letSystemName == null) {
			if (other.letSystemName != null)
				return false;
		} else if (!letSystemName.equals(other.letSystemName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ModelPartLotFilterDto [letSystemNamesFilter=" + letSystemName + ", dcPartNumbersFilter="
				+ dcPartNumber + ", dcNumbersFilter=" + dcNumber + "]";
	}
	
	

}
