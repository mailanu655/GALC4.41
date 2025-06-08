package com.honda.galc.entity.product;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>Base ProductSpec</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> Base ProductSpec description </p>
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
 * @author Paul Chou
 * Feb 18, 2014
 *
 */
public abstract class BaseProductSpec extends AuditEntry{

	private static final long serialVersionUID = 1L;

	/**
	 * Cannot have primary key productSpec here, otherwise openJPA will query both Frame and Engine table
	 * @return
	 */
	public abstract String getProductSpecCode();
	
	public abstract void setProductSpecCode(String productSpecCode);

}
