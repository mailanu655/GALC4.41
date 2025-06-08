package com.honda.galc.dto.lcvinbom;

import java.util.List;

import com.honda.galc.dto.IDto;

public class PartsByProductDto implements IDto {
	private static final long serialVersionUID = 1L;

	private String productId;
	private List<PartsDto> partsList;
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public List<PartsDto> getPartsList() {
		return partsList;
	}

	public void setPartsList(List<PartsDto> parts) {
		this.partsList = parts;
	}

	@Override
	public String toString() {
		return "PartsByProductDto [productId=" + productId + ", parts=" + partsList + "]";
	}
	
}
