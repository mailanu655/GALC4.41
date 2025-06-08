package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.ProductType;

/**
 * <h3>Class description</h3>
 * Case Class is used for transmission M-Case and TC-Case.
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Sep. 2, 2014</TD>
 * <TD>1.0</TD>
 * <TD>GY 20140902</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */
@Entity
@Table(name="CASE_TBX")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "PRODUCT_TYPE", discriminatorType=DiscriminatorType.STRING)
public abstract class Case extends DieCast {
	
	private static final long serialVersionUID = -6184090614541836571L;

	@Id
	@Column(name = "PRODUCT_ID")
    private String productId;

	@Column(name = "PRODUCT_TYPE")
	private String productType;

	public Case() {
		super();
	}

	public Case(ProductType productType){
		super();
		this.productType = productType.name();
	}
	
	public Case(ProductType productType, String productId) {
		super();
		this.productType = productType.name();
		this.productId = productId;
		setDcSerialNumber(productId);
	}

	public String getProductId() {
		return productId;
	}
	
	public void setProductId(String productId) {
		this.productId = productId;
	}
	

	public String getProductTypeValue() {
		return StringUtils.trim(productType);
	}

	@Override
	public ProductType getProductType() {
		return ProductType.getType(getProductTypeValue());
	}

	public Object getId() {
		return getProductId();
	}
}
