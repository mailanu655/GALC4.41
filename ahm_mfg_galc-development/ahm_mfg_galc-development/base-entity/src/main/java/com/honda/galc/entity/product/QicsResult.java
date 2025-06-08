package com.honda.galc.entity.product;

/**
 * 
 * <h3>QicsResult</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QicsResult description </p>
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
 * <TD>Mar 26, 2012</TD>
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
 * @since Mar 26, 2012
 */
public interface QicsResult {
	/**
	 * set error code
	 * @return
	 */
	public String getErrorCode();

	/**
	 * get error code
	 * @param errorCode
	 */
	public void setErrorCode(String errorCode);
	
}
