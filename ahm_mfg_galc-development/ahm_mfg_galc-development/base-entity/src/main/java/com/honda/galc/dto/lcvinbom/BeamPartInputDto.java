package com.honda.galc.dto.lcvinbom;

import com.honda.galc.dto.IDto;
import com.honda.galc.util.StringUtil;

public class BeamPartInputDto implements IDto {
	private static final long serialVersionUID = 1L;
	
	private String plantLocCode;
	private String division;
	
	public BeamPartInputDto() {
	}
	
	public String getPlantLocCode() {
		return plantLocCode;
	}
	public void setPlantLocCode(String plantLocCode) {
		this.plantLocCode = plantLocCode;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((division == null) ? 0 : division.hashCode());
		result = prime * result + ((plantLocCode == null) ? 0 : plantLocCode.hashCode());
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
		BeamPartInputDto other = (BeamPartInputDto) obj;
		if (division == null) {
			if (other.division != null)
				return false;
		} else if (!division.equals(other.division))
			return false;
		if (plantLocCode == null) {
			if (other.plantLocCode != null)
				return false;
		} else if (!plantLocCode.equals(other.plantLocCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return StringUtil.toString(getClass().getSimpleName(),
				getPlantLocCode(), getDivision());
	}
}
