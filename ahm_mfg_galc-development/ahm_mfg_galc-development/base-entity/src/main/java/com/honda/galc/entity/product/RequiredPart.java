package com.honda.galc.entity.product;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.AuditEntry;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name="REQUIRED_PARTS_TBX")
public class RequiredPart extends AuditEntry {
	@EmbeddedId
	@Auditable(isPartOfPrimaryKey= true,sequence=1)
	private RequiredPartId id;
	
	@Column(name = "SUB_ID")
	 @Auditable(isPartOfPrimaryKey= false,sequence=2)
    private String subId;
	
	@Column(name="PRODUCT_TYPE")
	 @Auditable(isPartOfPrimaryKey= false,sequence=3)
	private String productType;
	
	

	private static final long serialVersionUID = 1L;

	public RequiredPart() {
		super();
	}

	public RequiredPartId getId() {
		return this.id;
	}

	public void setId(RequiredPartId id) {
		this.id = id;
	}

	public String getSubId() {
		return StringUtils.trim(subId);
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}
	
	public String getProductType() {
		return StringUtils.trim(productType);
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getProductSpecCode() {
		return this.id.getProductSpecCode();
	}

	@Override
	public String toString() {
		return toString(id.getProcessPointId(),id.getProductSpecCodeValue(),id.getPartName());
	}
}
