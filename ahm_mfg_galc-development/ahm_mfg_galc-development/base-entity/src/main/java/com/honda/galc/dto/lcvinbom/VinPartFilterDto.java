package com.honda.galc.dto.lcvinbom;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;
import com.honda.galc.util.StringUtil;

public class VinPartFilterDto implements IDto {
	private static final long serialVersionUID = 1L;

	@DtoTag()
	private String letSystemName;
	@DtoTag()
	private String dcPartNumber;
	@DtoTag()
	private String productionLot;

	public VinPartFilterDto() {
	}

	public String getLetSystemName() {
		return letSystemName;
	}

	public void setLetSystemName(String letSystemName) {
		this.letSystemName = letSystemName;
	}

	public String getDcPartNumber() {
		return dcPartNumber;
	}

	public void setDcPartNumber(String dcPartNumber) {
		this.dcPartNumber = dcPartNumber;
	}

	public String getProductionLot() {
		return productionLot;
	}

	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dcPartNumber == null) ? 0 : dcPartNumber.hashCode());
		result = prime * result + ((letSystemName == null) ? 0 : letSystemName.hashCode());
		result = prime * result + ((productionLot == null) ? 0 : productionLot.hashCode());
		
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
		VinPartFilterDto other = (VinPartFilterDto) obj;
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
		if (productionLot == null) {
			if (other.productionLot != null)
				return false;
		} else if (!productionLot.equals(other.productionLot))
			return false;
		
		return true;
	}

	public String toString() {
		return StringUtil.toString(getClass().getSimpleName(), 
				getLetSystemName(), getDcPartNumber(), 
				 getProductionLot());
	}

}
