package com.honda.ahm.lc.vdb.dto;

import java.util.List;

import com.honda.ahm.lc.vdb.entity.IProductDetails;
import com.honda.ahm.lc.vdb.util.Constants;

public class LotDetailsDto {
	
	private String kdLotNumber;
    private String productionLot;
    private Integer count;
    private List<? extends IProductDetails> productList;
    
	public LotDetailsDto() {
		super();
	}
	
	public LotDetailsDto(String kdLotNumber, String productionLot, Integer count, List<? extends IProductDetails> productList) {
		super();
		this.kdLotNumber = kdLotNumber;
		this.productionLot = productionLot;
		this.count = count;
		this.productList = productList;
	}
	
	public String getKdLotNumber() {
		return kdLotNumber;
	}
	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}
	public String getProductionLot() {
		return productionLot;
	}
	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public List<? extends IProductDetails> getProductList() {
		return productList;
	}
	public void setProductList(List<? extends IProductDetails> productList) {
		this.productList = productList;
	}
	
	public String getUniqueKey() {
		return getKdLotNumber()+""+Constants.SEPARATOR+""+getProductionLot();
	}

	@Override
	public String toString() {
		String str = getClass().getSimpleName() + "{";
		str = str + "kdLotNumber: " + getKdLotNumber();
		str = str + ", productionLot: " + getProductionLot();
		str = str + ", count: " + getCount();
        str = str + ", productList: " + getProductList();
        str = str + "}";
        return str;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((count == null) ? 0 : count.hashCode());
		result = prime * result + ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + ((productList == null) ? 0 : productList.hashCode());
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
		LotDetailsDto other = (LotDetailsDto) obj;
		if (count == null) {
			if (other.count != null)
				return false;
		} else if (!count.equals(other.count))
			return false;
		if (kdLotNumber == null) {
			if (other.kdLotNumber != null)
				return false;
		} else if (!kdLotNumber.equals(other.kdLotNumber))
			return false;
		if (productList == null) {
			if (other.productList != null)
				return false;
		} else if (!productList.equals(other.productList))
			return false;
		if (productionLot == null) {
			if (other.productionLot != null)
				return false;
		} else if (!productionLot.equals(other.productionLot))
			return false;
		return true;
	}

}
