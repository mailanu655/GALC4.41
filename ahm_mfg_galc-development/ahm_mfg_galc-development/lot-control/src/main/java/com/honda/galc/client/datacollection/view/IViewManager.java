package com.honda.galc.client.datacollection.view;

import javax.swing.JPanel;
/**
 * 
 * <h3>IViewManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> IViewManager description </p>
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
 * <TD>Jan 26, 2012</TD>
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
 * @since Jan 26, 2012
 */
public interface IViewManager {
	JPanel getClientPanel();
	DataCollectionPanelBase getView();
	void setProductInputFocused();
}
