package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>ProductIdMask</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> Entity Class for table PRODUCT_ID_MASK_TBX </p>
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
 * @author Kamlesh Maharjan
 * March 05, 2016
 *
 */

@Entity
@Table(name="PRODUCT_ID_MASK_TBX")
public class ProductIdMask extends AuditEntry {
	@EmbeddedId
	private ProductIdMaskId id;
	
	@Column(name="PRODUCT_SPEC_CODE")
	private String productSpecCode;
	
	private static final long serialVersionUID = 1L;

	public ProductIdMask() {
		super();
	}

	public ProductIdMaskId getId() {
		return this.id;
	}

	public void setId(ProductIdMaskId id) {
		this.id = id;
	}
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
	
	public String getProductSpecCode(){
		return StringUtils.trim(productSpecCode);
	}
	
	@Override
	public String toString() {
		return toString(id.getProcessPointId(),id.getProductIdMask(),id.getProductType());
	}
}
