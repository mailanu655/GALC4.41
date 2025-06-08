package com.honda.galc.entity.product;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.util.ReflectionUtils;

/**
 * 
 * <h3>SubProduct Class description</h3>
 * <p> SubProduct description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * May 9, 2011
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name="SUB_PRODUCT_TBX")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "PRODUCT_TYPE", discriminatorType = DiscriminatorType.STRING)

//  REMARK : product_type column is defined as char(10),
//  open jpa when compares discriminator values does not trim whitespaces, and selects from db string with length = 10.
//  We need to pad discriminator(product type) to the total length of 10 to be matched with value stored in db.
//  For example : DiscriminatorValue(value = "MPDR      ")
//  If product_type column was defined as varchar(10) we would not have that problem (like for case entities).
//  See also DiscriminatorValue annotation in subclasses and setProductTypeValue(String productType) method.

public abstract class SubProduct extends Product {
	
	private static final long serialVersionUID = 1L;
    
	@Id
	@Column(name="PRODUCT_ID")
	private String productId;

	@Column(name = "PRODUCT_TYPE")
	private String productType;
	
	@Column(name = "SUB_ID")
	private String subId;
	
	@Column(name = "DEFECT_STATUS")
    private Integer defectStatus;

	@Column(name = "DUNNAGE")
	private String dunnage;
	
	@Transient
	private boolean isProductScrappable = true;
	
	public SubProduct() {
		super();
	}

	public SubProduct(ProductType productType) {
		super();
		setProductTypeValue(productType.name());
	}

	public SubProduct(ProductType productType, String productId) {
		this.productId = productId;
		setProductTypeValue(productType.name());
	}
	
	public String getProductId() {
		return StringUtils.trim(this.productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}


	public String getProductTypeValue() {
		return StringUtils.trim(productType);
	}

	// We do have here mismatch between property name and get/set method names. 
	private void setProductTypeValue(String productType) {
		//REMARK : product_type column is defined as char(10),
		// open jpa when compares discriminator values does not trim whitespaces, and selects from db string/length = 10.
		// we need to padd product type to the total length of 10 for discriminator to be matched with value stored in db.
		// if product_type column was defined as varchar(10) we would not have that problem (like for case entities).
		// See also discriminator values for SubProduct subclasses - they have to have length of 10, otherwise there will be no match.
		if (productType != null) {
			productType = StringUtils.rightPad(productType.trim(), 10, ' ');
		}
		this.productType = productType;
	}
	
	public ProductType getProductType() {
		return ProductType.getType(getProductTypeValue());
	}

	@Override
	public String getSubId() {
		return StringUtils.trim(subId);
	}

	@Override
	public void setSubId(String subId) {
		this.subId = subId;
	}
	
	/**
	 * return knuckle's part number prefix (substring from 0 to 11)
	 * @param productId
	 * @return
	 */
	public static String getPartNumberPrefix(String productId) {
		return StringUtils.substring(productId, 0, 11);
	}
	
	public static int getProductSerialNumber(String productId){
		return Integer.parseInt(productId.substring(productId.length() - 6));
	}
	
	public Integer getDefectStatusValue() {
		return defectStatus;
	}

	public void setDefectStatusValue(Integer defectStatus) {
		this.defectStatus = defectStatus;
	}
	
	@Override
	public String getDunnage() {
		return dunnage;
	}

	public void setDunnage(String dunnage) {
		this.dunnage = dunnage;
	}
	
	@Override
	public boolean isProductScrappable() {
		return this.isProductScrappable;
	}
	
	@Override
	public SubProduct clone() {
	
		SubProduct clone = ReflectionUtils.createInstance(getClass());
		clone.setProductId(this.getProductId());
		clone.setProductionLot(this.getProductionLot());
		clone.setKdLotNumber(this.getKdLotNumber());
		clone.setProductTypeValue(this.getProductTypeValue());
		clone.setProductSpecCode(this.getProductSpecCode());
		clone.setSubId(this.getSubId());
		clone.setPlanOffDate(this.getPlanOffDate());
		clone.setTrackingStatus(this.getTrackingStatus());
		clone.setProductStartDate(this.getProductStartDate());
		clone.setActualOffDate(this.getActualOffDate());
		clone.setAutoHoldStatus(this.getAutoHoldStatus());
		clone.setDefectStatusValue(this.getDefectStatusValue());
		clone.setProductionDate(this.getProductionDate());
		clone.setLastPassingProcessPointId(this.getLastPassingProcessPointId());
		clone.setDunnage(this.getDunnage());
		return clone;		
	}

	@Override
	public ProductNumberDef getProductNumberDef() {
		List<ProductNumberDef> defs = ProductNumberDef.getProductNumberDef(getProductType());
		return defs.isEmpty() ? null : defs.get(0);
	}

	@Override
	public String getOwnerProductId() {
		return null;
	}
}
