package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.*;

import org.apache.commons.lang.StringUtils;
/**
 * 
 * <h3>ProductTypeToNumberDefId</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductTypeToNumberDefId description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Feb 21, 2017</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Feb 21, 2017
 */
@Embeddable
public class ProductTypeToNumberDefId implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="MAIN_NO")
	private String mainNo;

	@Column(name="PRODUCT_ID_DEF")
	private String productIdDef;

	public ProductTypeToNumberDefId() {
	}
	public ProductTypeToNumberDefId(String mainNo, String productIdDef) {
		this.mainNo = mainNo;
		this.productIdDef = productIdDef;
	}
	public String getMainNo() {
		return StringUtils.trim(this.mainNo);
	}
	public void setMainNo(String mainNo) {
		this.mainNo = mainNo;
	}
	public String getProductIdDef() {
		return StringUtils.trim(this.productIdDef);
	}
	public void setProductIdDef(String productIdDef) {
		this.productIdDef = productIdDef;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ProductTypeToNumberDefId)) {
			return false;
		}
		ProductTypeToNumberDefId castOther = (ProductTypeToNumberDefId)other;
		return 
			this.mainNo.equals(castOther.mainNo)
			&& this.productIdDef.equals(castOther.productIdDef);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.mainNo.hashCode();
		hash = hash * prime + this.productIdDef.hashCode();
		
		return hash;
	}
}