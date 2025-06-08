package com.honda.galc.entity.product;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.data.ProductSpecCodeDef;

/**
 * 
 * <h3>ProductSpecCode Class description</h3>
 * <p> ProductSpecCode description </p>
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
@Table(name="PRODUCT_SPEC_CODE_TBX")
public class ProductSpecCode extends ProductSpec {
	@EmbeddedId
	private ProductSpecCodeId id;

	@Column(name="EXT_COLOR_CODE")
	private String extColorCode;

	@Column(name="INT_COLOR_CODE")
	private String intColorCode;

	private static final long serialVersionUID = 1L;

	public ProductSpecCode() {
		super();
	}

	public ProductSpecCodeId getId() {
		return this.id;
	}

	@Override
    public String getProductSpecCode() {
		return id == null ? "" : this.id.getProductSpecCode();
	}
	
	@Override
    public void setProductSpecCode(String productSpecCode) {
		if(id == null) id = new ProductSpecCodeId();
		id.setProductSpecCode(productSpecCode);
	}
	
	public void setId(ProductSpecCodeId id) {
		this.id = id;
	}

	public String getExtColorCode() {
		return this.extColorCode;
	}

	public void setExtColorCode(String extColorCode) {
		this.extColorCode = extColorCode;
	}

	public String getIntColorCode() {
		return this.intColorCode;
	}

	public void setIntColorCode(String intColorCode) {
		this.intColorCode = intColorCode;
	}

	@Override
	public int getProductNoPrefixLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String generateProductSpecCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(ProductSpecCodeDef.YEAR.format(getModelYearCode()));
		sb.append(ProductSpecCodeDef.MODEL.format(getModelCode()));
		sb.append(ProductSpecCodeDef.TYPE.format(getModelTypeCode()));
		sb.append(ProductSpecCodeDef.OPTION.format(getModelOptionCode()));
		sb.append(ProductSpecCodeDef.EXT_COLOR.format(getExtColorCode()));
		sb.append(ProductSpecCodeDef.INT_COLOR.format(getIntColorCode()));

		return sb.toString().trim();
	}

	public ProductSpecCodeDef validate() {
		if(!ProductSpecCodeDef.YEAR.validate(getModelYearCode())) return ProductSpecCodeDef.YEAR;
		if(!ProductSpecCodeDef.MODEL.validate(getModelCode())) return ProductSpecCodeDef.MODEL;
		if(!ProductSpecCodeDef.TYPE.validate(getModelTypeCode())) return ProductSpecCodeDef.TYPE;
		if(!ProductSpecCodeDef.OPTION.validate(getModelOptionCode())) return ProductSpecCodeDef.OPTION;
		if(!ProductSpecCodeDef.EXT_COLOR.validate(getExtColorCode())) return ProductSpecCodeDef.EXT_COLOR;
		if(!ProductSpecCodeDef.INT_COLOR.validate(getIntColorCode())) return ProductSpecCodeDef.INT_COLOR;
		
		return null;
		
	}

	@Override
	public String toString() {
		return toString(id.getProductSpecCode(),id.getProductType());
	}


}
