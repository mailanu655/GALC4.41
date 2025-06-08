package com.honda.galc.client.ui.event;

/**
 * 
 * <h3>ProductSpecCodeSelectionEvent</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductSpecCodeSelectionEvent description </p>
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
 * <TD>Apr 3, 2012</TD>
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
 * @since Apr 3, 2012
 */
public class ProductSpecCodeSelectionEvent extends SelectionEvent{
	public ProductSpecCodeSelectionEvent(Object source, int eventType) {
		super(source, eventType);
	}
}
