package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;

@Entity
@Table(name="CRANKSHAFT_TBX")
public class Crankshaft extends DieCast {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CRANKSHAFT_ID")
    private String crankshaftId;
	
	public Crankshaft(){
		super();
	}
	
	public Crankshaft(String crankshaftId) {
		super();
		this.crankshaftId = crankshaftId;
		setDcSerialNumber(crankshaftId);
	}

	public Object getId() {
		return getProductId();
	}

	public String getCrankshaftId() {
		return StringUtils.trim(crankshaftId);
	}
	
	public void setCrankshaftId(String crankshaftId) {
		this.crankshaftId = crankshaftId;
	}

	@Override
	public String getProductId() {
		return getCrankshaftId();
	}

	@Override
	public ProductNumberDef getProductNumberDef() {
		return ProductNumberDef.CS_HMA;
	}

	@Override
	public ProductType getProductType() {
		return ProductType.CRANKSHAFT;
	}

}
