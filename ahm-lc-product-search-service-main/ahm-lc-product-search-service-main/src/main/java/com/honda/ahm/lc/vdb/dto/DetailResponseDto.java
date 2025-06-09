package com.honda.ahm.lc.vdb.dto;


import java.util.List;

import com.honda.ahm.lc.vdb.entity.IProductDetails;

public class DetailResponseDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<LotDetailsDto> lotFormatData;
	private List<? extends IProductDetails> productFormatData;
	private Integer pageSize;
	private Integer pageIndex;
	private Long totalProductCount;
	
	public DetailResponseDto() {
		super();
	}
	
	public DetailResponseDto(List<LotDetailsDto> lotFormatData, List<? extends IProductDetails> productFormatData,Integer pageSize,Integer pageIndex,Long totalProductCount) {
		super();
		this.lotFormatData = lotFormatData;
		this.productFormatData = productFormatData;
		this.pageSize=pageSize;
		this.pageIndex=pageIndex;
		this.totalProductCount=totalProductCount;
	}

	public List<LotDetailsDto> getLotFormatData() {
		return lotFormatData;
	}
	public void setLotFormatData(List<LotDetailsDto> lotFormatData) {
		this.lotFormatData = lotFormatData;
	}
	public List<? extends IProductDetails> getProductFormatData() {
		return productFormatData;
	}
	public void setProductFormatData(List<? extends IProductDetails> productFormatData) {
		this.productFormatData = productFormatData;
	}
	
	

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Long getTotalProductCount() {
		return totalProductCount;
	}

	public void setTotalProductCount(Long totalProductCount) {
		this.totalProductCount = totalProductCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lotFormatData == null) ? 0 : lotFormatData.hashCode());
		result = prime * result + ((productFormatData == null) ? 0 : productFormatData.hashCode());
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
		DetailResponseDto other = (DetailResponseDto) obj;
		if (lotFormatData == null) {
			if (other.lotFormatData != null)
				return false;
		} else if (!lotFormatData.equals(other.lotFormatData))
			return false;
		if (productFormatData == null) {
			if (other.productFormatData != null)
				return false;
		} else if (!productFormatData.equals(other.productFormatData))
			return false;
		return true;
	}

}
