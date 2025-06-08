package com.honda.galc.entity.product;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductSpecCodeDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>ProductType Class description</h3>
 * <p> ProductType description </p>
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
 * Feb 16, 2012
 *
 *
 */
@Entity
@Table(name="PRODUCT_TYPE_TBX")
public class ProductTypeData extends AuditEntry {
	@Id
	@Column(name="PRODUCT_TYPE")
	private String productType;

	@Column(name="PRODUCT_TYPE_LABEL")
	private String productTypeLabel;

	@Column(name="PRODUCT_SPEC_CODE_FORMAT")
	private String productSpecCodeFormat;

	@Column(name="PRODUCT_SPEC_CODE_LABEL")
	private String productSpecCodeLabel;

	@Column(name="OWNER_PRODUCT_TYPE")
	private String ownerProductType;

	@Column(name="PRODUCT_ID_LABEL")
	private String productIdLabel;

	@Column(name="PRODUCT_ID_FORMAT")
	private String productIdFormat;
	
	private static final long serialVersionUID = 1L;

	

	public ProductTypeData() {
		super();
	}
	
	public String getId() {
		return getProductTypeName();
	}


	public ProductType getProductType() {
		if(ProductType.contains(getProductTypeName()))
			return ProductType.valueOf(getProductTypeName());
		else
			 return ProductType.MBPN_PART;
		
	}
	
	
	public String getProductTypeName() {
		return StringUtils.trim(this.productType);
	}

	public void setProductTypeName(String productType) {
		this.productType = productType;
	}

	public String getProductTypeLabel() {
		return StringUtils.trim(this.productTypeLabel);
	}

	public void setProductTypeLabel(String productTypeLabel) {
		this.productTypeLabel = productTypeLabel;
	}

	public String getProductSpecCodeFormat() {
		return StringUtils.trim(this.productSpecCodeFormat);
	}

	public void setProductSpecCodeFormat(String productSpecCodeFormat) {
		this.productSpecCodeFormat = productSpecCodeFormat;
	}

	public String getProductSpecCodeLabel() {
		return StringUtils.trim(this.productSpecCodeLabel);
	}

	public void setProductSpecCodeLabel(String productSpecCodeLabel) {
		this.productSpecCodeLabel = productSpecCodeLabel;
	}

	public String getOwnerProductTypeName() {
		return StringUtils.trim(this.ownerProductType);
	}
	
	public ProductType getOwnerProductType() {
		return ProductType.getType(getOwnerProductTypeName()); 
	}
	public void setOwnerProductTypeName(String ownerProductType) {
		this.ownerProductType = ownerProductType;
	}

	public String getProductIdLabel() {
		return StringUtils.trim(this.productIdLabel);
	}

	public void setProductIdLabel(String productIdLabel) {
		this.productIdLabel = productIdLabel;
	}

	public String getProductIdFormatType() {
		return StringUtils.trim(this.productIdFormat);
	}

	public void setProductIdFormatType(String productIdFormat) {
		this.productIdFormat = productIdFormat;
	}
	
	public List<ProductSpecCodeDef> getProductSpecCodeDefs(){
		List<ProductSpecCodeDef> defList = new ArrayList<ProductSpecCodeDef>();
		
		if(getProductSpecCodeFormat() != null){
			for(char c : getProductSpecCodeFormat().toCharArray()){
				defList.add(ProductSpecCodeDef.valueOfCode(c));
			}
		}
		
		return defList;
	}
	
	public List<ProductNumberDef> getProductNumberDefs() {
		return ProductNumberDef.getProductNumberDefs(getProductIdFormatType());
	}
	
	public boolean isNumberValid(String number) {
		if(getProductNumberDefs() == null || getProductNumberDefs().size() == 0) return true;
		for(ProductNumberDef def : getProductNumberDefs()) {
			if(def.isNumberValid(number)) return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return toString(getProductTypeName(),getProductIdFormatType());
	}


}
