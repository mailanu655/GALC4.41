package com.honda.galc.entity.product;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;
/**
 * 
 * <h3>ProductTypeToNumberDef</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductTypeToNumberDef description </p>
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
@Table(name="PRODUCT_TYPE_TO_NUMBER_DEF_TBX")
public class ProductTypeToNumberDef extends AuditEntry {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ProductTypeToNumberDefId id;
	
	@OneToOne(targetEntity = ProductIdNumberDef.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST,CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(name="PRODUCT_ID_DEF",referencedColumnName="PRODUCT_ID_DEF",updatable=false,insertable=false)
	ProductIdNumberDef productIdNumberDef;
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "MAIN_NO", referencedColumnName = "MAIN_NO",
                    unique = true, nullable = false, insertable = false, updatable = false)
    })
	private MbpnProductType mbpnProductType;


	public ProductTypeToNumberDef() {
	}

	public ProductTypeToNumberDef(String mainNo, String productIdDef) {
		this.id = new ProductTypeToNumberDefId(mainNo, productIdDef);
	}

	public ProductTypeToNumberDefId getId() {
		return this.id;
	}

	public void setId(ProductTypeToNumberDefId id) {
		this.id = id;
	}
	
	public ProductIdNumberDef getProductIdNumberDef() {
		return productIdNumberDef;
	}

	public void setProductIdNumberDef(ProductIdNumberDef productIdNumberDef) {
		this.productIdNumberDef = productIdNumberDef;
	}


}