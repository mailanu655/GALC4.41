package com.honda.galc.entity.product;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>MbpnProductType</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> MbpnProductType description </p>
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
@Entity
@Table(name="MBPN_PRODUCT_TYPE_TBX")
public class MbpnProductType extends AuditEntry {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MbpnProductTypeId id;
	
	@Column(name="PRODUCT_TYPE")
	private String productType;

	public MbpnProductType() {
	}
	
	public MbpnProductType(String mainNo, String ownerMainNo) {
		this.id = new MbpnProductTypeId(mainNo, ownerMainNo);
	}
	
	public MbpnProductType(String mainNo, String ownerMainNo, String productType) {
		this(mainNo, ownerMainNo);
		this.productType = productType;
	}

	@OneToMany(targetEntity = ProductTypeToNumberDef.class, mappedBy = "mbpnProductType", cascade = {}, fetch = FetchType.EAGER)
	List<ProductTypeToNumberDef> productTypeToNumberDefs;
	
	public List<ProductTypeToNumberDef> getProductTypeToNumberDefs() {
		return productTypeToNumberDefs;
	}

	public void setProductTypeToNumberDef(
			List<ProductTypeToNumberDef> productTypeToNumberDefs) {
		this.productTypeToNumberDefs = productTypeToNumberDefs;
	}
	
	public List<ProductIdNumberDef> getProductIdNumberDefs(){
		List<ProductIdNumberDef> list = new ArrayList<ProductIdNumberDef>();
		if(null != getProductTypeToNumberDefs() && getProductTypeToNumberDefs().size() > 0) {
			for(ProductTypeToNumberDef def : getProductTypeToNumberDefs())
				list.add(def.getProductIdNumberDef());
		}
		return list;
	}

	public MbpnProductTypeId getId() {
		if(this.id == null)
			this.id = new MbpnProductTypeId("","");
		return this.id;
	}

	public void setId(MbpnProductTypeId id) {
		this.id = id;
	}


	public String getProductType() {
		return StringUtils.trimToEmpty(productType);
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
	

	@Override
	public String toString() {
		return this.getId().getMainNo();
	}

	
}