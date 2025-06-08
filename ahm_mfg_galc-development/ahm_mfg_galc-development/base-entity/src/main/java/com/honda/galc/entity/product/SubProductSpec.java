package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.PrintAttribute;

/**
 * 
 * <h3>FrameSpec Class description</h3>
 * <p> FrameSpec description </p>
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
public class SubProductSpec extends ProductSpec {
	
	@Id
	@Column(name = "PRODUCT_SPEC_CODE")
	private String productSpecCode;
	
	@Column(name="EXT_COLOR_CODE")
	private String extColorCode;

	@Column(name="INT_COLOR_CODE")
	private String intColorCode;

	private static final long serialVersionUID = 1L;

	public SubProductSpec() {
		super();
	}
	
    @Override
    public String getProductSpecCode() {
		return StringUtils.trim(this.productSpecCode);
	}
    
    @Override
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
    
    @PrintAttribute
	public String getExtColorCode() {
		return StringUtils.trim(this.extColorCode);
	}

	public void setExtColorCode(String extColorCode) {
		this.extColorCode = extColorCode;
	}

	@PrintAttribute
	public String getIntColorCode() {
		return StringUtils.trim(this.intColorCode);
	}

	public void setIntColorCode(String intColorCode) {
		this.intColorCode = intColorCode;
	}

	@Override
	public int getProductNoPrefixLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object getId() {
		// TODO Auto-generated method stub
		return null;
	}	

}
