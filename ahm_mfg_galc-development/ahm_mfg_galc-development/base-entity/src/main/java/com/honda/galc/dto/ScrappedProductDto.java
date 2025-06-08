package com.honda.galc.dto;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ExceptionalOut;

public class ScrappedProductDto implements IDto {
	private static final long serialVersionUID = 1L;
	
	private BaseProduct product;
	private ExceptionalOut exceptionalOut;
	private String processPointName;
	private boolean isNaqScrap;
	
	public BaseProduct getProduct() {
		return this.product;
	}
	
	public void setProduct(BaseProduct product) {
		this.product = product;
	}

	public String getProcessPointName() {
		return StringUtils.trim(this.processPointName);
	}
	
	public void setProcessPointName(String processPointName) {
		this.processPointName = processPointName;
	}
	
	public ExceptionalOut getExceptionalOut() {
		return this.exceptionalOut;
	}
	
	public void setExceptionalOut(ExceptionalOut exceptionalOut) {
		this.exceptionalOut = exceptionalOut;
	}
	
	public boolean isNaqScrap() {
		return this.isNaqScrap;
	}

	public void setNaqScrap(boolean isNaqScrap) {
		this.isNaqScrap = isNaqScrap;
	}
}
