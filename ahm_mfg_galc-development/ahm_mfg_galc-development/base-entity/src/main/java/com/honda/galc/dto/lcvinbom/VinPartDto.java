package com.honda.galc.dto.lcvinbom;

import com.honda.galc.dto.IDto;
import com.honda.galc.util.StringUtil;

public class VinPartDto implements IDto {
	private static final long serialVersionUID = 1L;

	private String productId;
	private String letSystemName;
	private String dcPartNumber;
	private boolean shipStatus;
	private String productSpecCode;
	private String productionLot;

	public VinPartDto() {
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
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

	public boolean getShipStatus() {
		return shipStatus;
	}

	public void setShipStatus(boolean shipStatus) {
		this.shipStatus = shipStatus;
	}

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
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
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + ((productionLot == null) ? 0 : productionLot.hashCode());
		result = prime * result + (shipStatus ? 1231 : 1237);
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
		VinPartDto other = (VinPartDto) obj;
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
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (productSpecCode == null) {
			if (other.productSpecCode != null)
				return false;
		} else if (!productSpecCode.equals(other.productSpecCode))
			return false;
		if (productionLot == null) {
			if (other.productionLot != null)
				return false;
		} else if (!productionLot.equals(other.productionLot))
			return false;
		if (shipStatus != other.shipStatus)
			return false;
		return true;
	}

	public String toString() {
		return StringUtil.toString(getClass().getSimpleName(), 
				getProductId(), getLetSystemName(), getDcPartNumber(), 
				getShipStatus(), getProductSpecCode(), getProductionLot());
	}

}
