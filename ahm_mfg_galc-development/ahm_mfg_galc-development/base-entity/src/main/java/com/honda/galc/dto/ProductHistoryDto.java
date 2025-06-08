package com.honda.galc.dto;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

public class ProductHistoryDto implements IDto {

	private static final long serialVersionUID = 1L;
	
	@DtoTag(name = "PRODUCT_ID")
	private String productId;

	@DtoTag(name =  "ASSOCIATE_NO")
    private String associateNo;
	
	@DtoTag(name = "PRODUCT_SPEC_CODE")
    private String productSpecCode;

	@DtoTag(name = "ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;

	public String getProductId() {
		return StringUtils.trimToEmpty(this.productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getAssociateNo() {
		return StringUtils.trimToEmpty(this.associateNo);
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}

	public String getProductSpecCode() {
		return StringUtils.trimToEmpty(this.productSpecCode);
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public Timestamp getActualTimestamp() {
		return actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}
	
}
