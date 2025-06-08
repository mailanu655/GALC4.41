package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
/**
 * 
 * <h3>ProductIdNumberDef</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductIdNumberDef description </p>
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
@Table(name="PRODUCT_NUMBER_DEF_TBX")
public class ProductIdNumberDef extends AuditEntry {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PRODUCT_ID_DEF")
	private String productIdDef;


	@Column(name="LENGTH")
	private short length;

	private String mask;

	public ProductIdNumberDef() {
	}

	public ProductIdNumberDef(String id, short length, String mask) {
		this.productIdDef = id;
		this.length = length;
		this.mask = mask;
	}

	public String getProductIdDef() {
		return StringUtils.trimToEmpty(this.productIdDef);
	}

	public void setProductIdDef(String productIdDef) {
		this.productIdDef = productIdDef;
	}

	public short getLength() {
		return this.length;
	}

	public void setLength(short length) {
		this.length = length;
	}

	public String getMask() {
		return StringUtils.trimToEmpty(this.mask);
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public String getId() {
		return productIdDef;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
        sb.append("\"").append(this.getProductIdDef()).append("\"");
        sb.append(",\"").append(this.getLength()).append("\"");
        sb.append(",\"").append(this.getMask()).append("\"");
       return sb.toString();
	}
	
	

}