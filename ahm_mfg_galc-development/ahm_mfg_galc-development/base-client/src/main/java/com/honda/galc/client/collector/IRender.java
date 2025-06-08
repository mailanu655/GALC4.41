package com.honda.galc.client.collector;
/**
 * 
 * <h3>IRender</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> IRender description </p>
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
 * <TD>Dec 5, 2011</TD>
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
 * @since Dec 5, 2011
 */
public interface IRender<T> {
	/**
	 * Render target component
	 * @return
	 */
	public Object render();

	/**
	 * Set render instruction detail information property
	 * @param tmp
	 */
	public void setProperty(T property);
}
