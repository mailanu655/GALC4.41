package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;

@Entity
@Table(name="CONROD_TBX")
public class Conrod extends DieCast {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CONROD_ID")
    private String conrodId;
	
	public Conrod(){
		super();
	}
	
	public Conrod(String conrodId) {
		super();
		this.conrodId = conrodId;
		setDcSerialNumber(conrodId);
	}

	public Object getId() {
		return getProductId();
	}

	public String getConrodId() {
		return StringUtils.trim(conrodId);
	}
	
	public void setConrodId(String conrodId) {
		this.conrodId = conrodId;
	}

	@Override
	public String getProductId() {
		return getConrodId();
	}

	@Override
	public ProductNumberDef getProductNumberDef() {
		return ProductNumberDef.CR_HMA;
	}

	@Override
	public ProductType getProductType() {
		return ProductType.CONROD;
	}

}
