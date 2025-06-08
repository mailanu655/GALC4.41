package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.util.StringUtil;

/**
 * The persistent class for the GAL268TBX database table.
 * 
 */
@Entity
@Table(name="GAL268TBX")
public class FrameMTOCPriceMasterSpec  extends AuditEntry{

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private FrameMTOCPriceMasterSpecId id;
	
	@Column(name="CURRENCY")
	private String currency;
	
	@Column(name="PRICE")
	private String price;

	@Column(name="PRICE_TYPE")
	private String priceType;

	@Column(name="QUOTATION_NO")
	private String quotationNo;

	public FrameMTOCPriceMasterSpec() {
	}
	
	public FrameMTOCPriceMasterSpecId getId() {
		return this.id;
	}

	public void setId(FrameMTOCPriceMasterSpecId id) {
		this.id = id;
	}

	public String getCurrency() {
		return StringUtils.trim(this.currency);
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPrice() {
		return StringUtils.trim(this.price);
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPriceType() {
		return StringUtils.trim(this.priceType);
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public String getQuotationNo() {
		return StringUtils.trim(this.quotationNo);
	}

	public void setQuotationNo(String quotationNo) {
		this.quotationNo = quotationNo;
	}
	
	@Override
	public String toString(){
		return StringUtil.toString(
				getId().getPlantCodeFrame(), 
				getId().getModelYearCode(), 
				getId().getModelCode(), 
				getId().getModelTypeCode(), 
				getId().getModelOptionCode(), 
				getId().getExtColorCode(),
				getId().getIntColorCode(), 
				getId().getEffectiveDate()
				);
	}
}